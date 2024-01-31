package ru.cft.template.model.request;

public record MaintenanceBody(
        Long phone,
        Long amount,
        String comment
) {
}
