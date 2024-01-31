package ru.cft.template.model;

import java.util.Date;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        Long amount,
        Date lastUpdate
) {
}
