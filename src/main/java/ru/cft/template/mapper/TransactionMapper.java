package ru.cft.template.mapper;

import ru.cft.template.entity.Transaction;
import ru.cft.template.model.response.TransactionHistoryResponse;

public class TransactionMapper {
    public static TransactionHistoryResponse mapTransactionToHistory(Transaction transaction) {
        return new TransactionHistoryResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getType(),
                transaction.getReceiverPhone(),
                transaction.getMaintenanceNumber(),
                transaction.getStatus()
        );
    }
}
