package ru.cft.template.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cft.template.entity.Transaction;
import ru.cft.template.entity.Wallet;
import ru.cft.template.model.TransactionType;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySenderWallet(Wallet senderWallet);
    List<Transaction> findBySenderWalletAndType(Wallet senderWallet, TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.senderWallet.id = :walletId AND t.type = :type ORDER BY t.transactionDate DESC")
    List<Transaction> findLastTransactionsByTypeAndWalletId(TransactionType type, UUID walletId, Pageable pageable);
}
