package ru.cft.template.mapper;

import ru.cft.template.entity.Maintenance;
import ru.cft.template.model.MaintenanceStatus;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.response.MaintenanceInfoResponse;

public class MaintenanceMapper {
    public static MaintenanceInfoResponse mapMaintenanceToResponse(
            Maintenance maintenance,
            MaintenanceType type
    ){
        return new MaintenanceInfoResponse(
                maintenance.getId(),
                type,
                maintenance.getAmount(),
                maintenance.getMaintenanceNumber(),
                maintenance.getStatus(),
                maintenance.getTransactionDate()
        );
    }
}
