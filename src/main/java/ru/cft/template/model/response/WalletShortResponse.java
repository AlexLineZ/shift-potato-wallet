package ru.cft.template.model.response;

import java.util.UUID;

public record WalletShortResponse(
        UUID billId,
        Long amount
) { }
