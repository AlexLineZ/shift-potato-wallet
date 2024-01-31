package ru.cft.template.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Maintenance;
import ru.cft.template.entity.Transaction;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.exception.BadTransactionException;
import ru.cft.template.exception.WalletNotFoundException;
import ru.cft.template.model.MaintenanceStatus;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.TransactionStatus;
import ru.cft.template.model.TransactionType;
import ru.cft.template.model.request.MaintenanceBody;
import ru.cft.template.model.request.TransferBody;
import ru.cft.template.model.response.CreatedMaintenanceResponse;
import ru.cft.template.repository.MaintenanceRepository;
import ru.cft.template.repository.TransactionRepository;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.repository.WalletRepository;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Transactional
    public Transaction processTransaction(Authentication authentication, TransferBody request) {
        User user = userService.getUserById(authentication);
        Wallet senderWallet = user.getWallet();

        Transaction transaction = new Transaction();

        transaction.setAmount(request.amount());
        transaction.setTransactionDate(new Date());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setSenderWallet(senderWallet);

        if (request.receiverPhone() != null) {
            transaction.setType(TransactionType.TRANSFER);
            GetValidTransaction(request, senderWallet, transaction);

            Wallet receiverWallet = findWalletByPhone(request.receiverPhone());
            if (receiverWallet == null) {
                throw new BadTransactionException("Receiver wallet not found");
            } else {
                receiverWallet.setAmount(receiverWallet.getAmount() + request.amount());
            }

            walletRepository.save(receiverWallet);

        } else if (request.maintenanceNumber() != null) {
            transaction.setType(TransactionType.PAYMENT);
            GetValidTransaction(request, senderWallet, transaction);

            transaction.setMaintenanceNumber(request.maintenanceNumber());

            //нужно сделать счета

        } else {
            throw new BadTransactionException("Invalid transaction request");
        }

        transaction.setSenderWallet(senderWallet);
        senderWallet.setAmount(senderWallet.getAmount() - request.amount());
        walletRepository.save(senderWallet);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);
        return transaction;
    }


    public CreatedMaintenanceResponse createMaintenance(Authentication authentication, MaintenanceBody body){
        User user = userService.getUserById(authentication);
        Wallet senderWallet = user.getWallet();
        if (senderWallet == null) {
            throw new WalletNotFoundException("Sender wallet not found");
        }

        Wallet receiverWallet = findWalletByPhone(body.phone());
        if (receiverWallet == null) {
            throw new WalletNotFoundException("Receiver wallet not found");
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

    private Wallet findWalletByPhone(Long phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new BadTransactionException("User not found for the given phone number"));
        return user.getWallet();
    }
}
