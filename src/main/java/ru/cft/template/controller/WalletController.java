package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.model.response.WalletResponse;
import ru.cft.template.service.WalletService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/wallet/bill")
    public ResponseEntity<WalletResponse> getUser(Authentication authentication) {
        return ResponseEntity.ok(walletService.getUserWallet(authentication));
    }

}
