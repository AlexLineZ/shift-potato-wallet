package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.mapper.WalletMapper;
import ru.cft.template.model.response.WalletResponse;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.repository.WalletRepository;
import ru.cft.template.jwt.JwtTokenUtils;
import ru.cft.template.service.WalletService;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public WalletResponse getUserWallet(Authentication authentication){
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        return WalletMapper.mapWalletToResponse(user.getWallet());
    }

    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.setAmount(0L);
        wallet.setLastUpdate(new Date());

        return walletRepository.save(wallet);
    }
}
