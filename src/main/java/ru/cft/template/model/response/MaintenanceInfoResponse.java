package ru.cft.template.model.response;

import ru.cft.template.model.MaintenanceStatus;
import ru.cft.template.model.MaintenanceType;

import java.util.Date;
import java.util.UUID;

public record MaintenanceInfoResponse(
        UUID id,
        MaintenanceType type,
        Long amount,
        Long maintenanceNumber,
        MaintenanceStatus status,
        Date transactionDate
) {
}
