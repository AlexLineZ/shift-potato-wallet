package ru.cft.template.model.response;

import ru.cft.template.model.TransactionStatus;
import ru.cft.template.model.TransactionType;

import java.util.Date;
import java.util.UUID;

public record TransactionHistoryResponse(
        UUID id,
        Long amount,
        Date transactionDate,
        TransactionType type,
        Long receiverPhone,
        Long maintenanceNumber,
        TransactionStatus status
) {
}
