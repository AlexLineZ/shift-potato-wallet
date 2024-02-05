package ru.cft.template.model.response;

import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID userId,
        WalletShortResponse wallet
) {
}
