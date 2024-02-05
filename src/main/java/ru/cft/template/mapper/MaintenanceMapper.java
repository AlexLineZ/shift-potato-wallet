package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.Maintenance;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.response.MaintenanceInfoResponse;

@Component
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
