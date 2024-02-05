package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.Wallet;
import ru.cft.template.model.response.WalletResponse;

@Component
public class WalletMapper {
    public static WalletResponse mapWalletToResponse(Wallet wallet){
        return new WalletResponse(
                wallet.getId(),
                wallet.getAmount(),
                wallet.getLastUpdate()
        );
    }
}
