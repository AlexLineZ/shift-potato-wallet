package ru.cft.template.service;

import org.springframework.security.core.Authentication;
import ru.cft.template.entity.Wallet;
import ru.cft.template.model.response.WalletResponse;

public interface WalletService {
    WalletResponse getUserWallet(Authentication authentication);

    Wallet createWallet();
}
