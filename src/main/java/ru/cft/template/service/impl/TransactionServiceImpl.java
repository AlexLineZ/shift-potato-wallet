package ru.cft.template.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Maintenance;
import ru.cft.template.entity.Transaction;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.exception.BadTransactionException;
import ru.cft.template.exception.InsufficientFundsException;
import ru.cft.template.exception.WalletNotFoundException;
import ru.cft.template.mapper.MaintenanceMapper;
import ru.cft.template.mapper.TransactionMapper;
import ru.cft.template.model.MaintenanceStatus;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.TransactionStatus;
import ru.cft.template.model.TransactionType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.MaintenanceBody;
import ru.cft.template.model.request.TransferBody;
import ru.cft.template.model.response.*;
import ru.cft.template.repository.MaintenanceRepository;
import ru.cft.template.repository.TransactionRepository;
import ru.cft.template.repository.WalletRepository;
import ru.cft.template.service.TransactionService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserServiceImpl userService;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final MaintenanceRepository maintenanceRepository;

    public WalletShortResponse hesoyam(Authentication authentication, AmountBody body){

        if (body.amount() == null){
            throw new BadTransactionException("you need to indicate the amount of money");
        }

        if (body.amount() > 1000000) {
            throw new BadTransactionException("A lot of money my friend");
        }

        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();

        List<Transaction> lastRefillTransactions = transactionRepository
                .findLastTransactionsByTypeAndWalletId(
                        TransactionType.REFILL,
                        senderWallet.getId(),
                        PageRequest.of(0, 4)
                );

        if (!lastRefillTransactions.isEmpty()) {
            Transaction lastTransaction = lastRefillTransactions.get(0);
            long timeSinceLastAttempt = new Date().getTime() - lastTransaction.getTransactionDate().getTime();

            if (timeSinceLastAttempt < 30000) {
                throw new BadTransactionException("Ohladite svoe dihanie");
            }
        }

        Transaction refillTransaction = new Transaction();
        refillTransaction.setSenderWallet(senderWallet);
        refillTransaction.setType(TransactionType.REFILL);
        refillTransaction.setTransactionDate(new Date());
        refillTransaction.setStatus(shouldRefill(lastRefillTransactions)
                ? TransactionStatus.SUCCESSFUL : TransactionStatus.FAILED);

        if (TransactionStatus.SUCCESSFUL.equals(refillTransaction.getStatus())) {
            senderWallet.setAmount(senderWallet.getAmount() + body.amount());
            refillTransaction.setAmount(body.amount());
            walletRepository.save(senderWallet);
        } else {
            refillTransaction.setAmount(body.amount());
        }

        transactionRepository.save(refillTransaction);

        return new WalletShortResponse(
                senderWallet.getId(),
                senderWallet.getAmount()
        );
    }

    private boolean shouldRefill(List<Transaction> transactions) {
        Random random = new Random();
        if (transactions.size() >= 3 && transactions.stream()
                .filter(t -> TransactionStatus.FAILED.equals(t.getStatus())).count() >= 3) {
            return true;
        }
        return random.nextInt(4) == 0;
    }

    public List<TransactionHistoryResponse> getHistory(Authentication authentication, TransactionType type) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet userWallet = user.getWallet();

        List<Transaction> transactions;
        if (type == null) {
            transactions = transactionRepository.findBySenderWallet(userWallet);
        } else {
            transactions = transactionRepository.findBySenderWalletAndType(userWallet, type);
        }

        return transactions.stream()
                .map(TransactionMapper::mapTransactionToHistory)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponse processTransaction(Authentication authentication, TransferBody request) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();

        Transaction transaction = new Transaction();

        transaction.setAmount(request.amount());
        transaction.setTransactionDate(new Date());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setSenderWallet(senderWallet);

        if (request.receiverPhone() != null) {
            createTransfer(transaction, request, senderWallet);
        } else if (request.maintenanceNumber() != null) {
            createPayment(transaction, request, senderWallet);
        } else {
            throw new BadTransactionException("Invalid transaction request");
        }

        senderWallet.setAmount(senderWallet.getAmount() - request.amount());
        walletRepository.save(senderWallet);

        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);

        WalletShortResponse wallet = new WalletShortResponse(
                senderWallet.getId(),
                senderWallet.getAmount()
        );

        return new TransactionResponse(
                transaction.getId(),
                user.getId(),
                wallet
        );
    }

    private void createTransfer(
            Transaction transaction,
            TransferBody request,
            Wallet senderWallet
    ) {
        transaction.setType(TransactionType.TRANSFER);
        Wallet receiverWallet = userService.findUserByPhone(request.receiverPhone()).getWallet();

        if (receiverWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransaction(request, senderWallet, transaction);

        receiverWallet.setAmount(receiverWallet.getAmount() + request.amount());
        walletRepository.save(receiverWallet);
    }

    private void createPayment(
            Transaction transaction,
            TransferBody request,
            Wallet senderWallet
    ){
        transaction.setType(TransactionType.PAYMENT);
        GetValidTransaction(request, senderWallet, transaction);

        Optional<Maintenance> maintenanceOptional = maintenanceRepository
                .findByMaintenanceNumber(request.maintenanceNumber());

        if (maintenanceOptional.isPresent()) {
            Maintenance maintenance = maintenanceOptional.get();
            if (maintenance.getStatus() == MaintenanceStatus.PAID) {
                throw new IllegalStateException("This maintenance is already paid");
            }
            if (senderWallet.getAmount() < maintenance.getAmount()) {
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                throw new InsufficientFundsException("Insufficient funds for payment");
            }

            Wallet receiverWallet = maintenance.getReceiverWallet();

            if (maintenance.getSenderWallet() == senderWallet || maintenance.getReceiverWallet() != senderWallet) {
                throw new AccessRightsException("You are not authorized to pay this maintenance");
            }

            receiverWallet.setAmount(receiverWallet.getAmount() + request.amount());
            walletRepository.save(receiverWallet);

            maintenance.setStatus(MaintenanceStatus.PAID);
        } else {
            throw new EntityNotFoundException("Maintenance not found");
        }
        transaction.setMaintenanceNumber(request.maintenanceNumber());
    }


    public CreatedMaintenanceResponse createMaintenance(Authentication authentication, MaintenanceBody body){
        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();
        if (senderWallet == null) {
            throw new WalletNotFoundException("Sender wallet not found");
        }

        Wallet receiverWallet = userService.findUserByPhone(body.phone()).getWallet();
        if (receiverWallet == null) {
            throw new WalletNotFoundException("Receiver wallet not found");
        }

        if (senderWallet == receiverWallet) {
            throw new BadTransactionException("You can't bill yourself");
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setSenderWallet(senderWallet);
        maintenance.setMaintenanceNumber(System.currentTimeMillis());
        maintenance.setReceiverWallet(receiverWallet);
        maintenance.setAmount(body.amount());
        maintenance.setStatus(MaintenanceStatus.UNPAID);
        maintenance.setTransactionDate(new Date());
        maintenance.setComment(body.comment());

        maintenanceRepository.save(maintenance);
        return new CreatedMaintenanceResponse(
                maintenance.getId(),
                maintenance.getMaintenanceNumber(),
                maintenance.getStatus()
        );
    }

    public List<MaintenanceInfoResponse> getUserMaintenances(Authentication authentication, MaintenanceType type) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet userWallet = user.getWallet();

        List<Maintenance> maintenances;
        if (type == null) {
            maintenances = maintenanceRepository.findBySenderWalletOrReceiverWallet(userWallet, userWallet);
        } else {
            maintenances = switch (type) {
                case INBOUND -> maintenanceRepository.findByReceiverWallet(userWallet);
                case OUTBOUND -> maintenanceRepository.findBySenderWallet(userWallet);
                default -> throw new IllegalArgumentException("Unexpected value: " + type);
            };
        }

        return maintenances.stream()
                .map(maintenance -> {
                    MaintenanceType responseType = maintenance.getSenderWallet().equals(userWallet)
                            ? MaintenanceType.OUTBOUND
                            : MaintenanceType.INBOUND;
                    return MaintenanceMapper.mapMaintenanceToResponse(maintenance, responseType);
                }).collect(Collectors.toList());
    }

    private void GetValidTransaction(TransferBody request, Wallet senderWallet, Transaction transaction) {
        if (!isValidTransaction(request)){
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new BadTransactionException("Invalid transaction request");
        }

        if (!isValidWallet(request, senderWallet)){
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new BadTransactionException("Insufficient funds or wallet not found");
        }
    }

    private boolean isValidWallet(TransferBody request, Wallet senderWallet){
        return senderWallet != null && senderWallet.getAmount() >= request.amount();
    }

    private boolean isValidTransaction(TransferBody body) {
        if (body.amount() == null || body.amount() <= 0) {
            return false;
        }

        return (body.receiverPhone() == null || body.maintenanceNumber() == null) &&
                (body.receiverPhone() != null || body.maintenanceNumber() != null);
    }
}
