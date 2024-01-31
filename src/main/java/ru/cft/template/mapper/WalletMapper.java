package ru.cft.template.mapper;

import ru.cft.template.entity.Wallet;
import ru.cft.template.model.WalletResponse;

public class WalletMapper {
    public static WalletResponse mapWalletToResponse(Wallet wallet){
        return new WalletResponse(
                wallet.getId(),
                wallet.getAmount(),
                wallet.getLastUpdate()
        );
    }
}
