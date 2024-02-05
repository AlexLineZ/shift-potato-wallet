package ru.cft.template.model.request;

public record TransferBody(
        Long receiverPhone,
        Long maintenanceNumber,
        Long amount
) {
}
