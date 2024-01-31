package ru.cft.template.model;

import java.util.UUID;

public record WalletShortResponse(
        UUID billId,
        Long amount
) { }
