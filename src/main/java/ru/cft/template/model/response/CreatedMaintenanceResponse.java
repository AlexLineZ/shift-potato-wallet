package ru.cft.template.model.response;

import ru.cft.template.model.MaintenanceStatus;

import java.util.UUID;

public record CreatedMaintenanceResponse(
        UUID id,
        Long maintenanceNumber,
        MaintenanceStatus status
) {
}
