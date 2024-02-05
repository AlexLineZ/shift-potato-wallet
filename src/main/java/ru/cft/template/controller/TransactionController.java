package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.TransactionType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.MaintenanceBody;
import ru.cft.template.model.request.TransferBody;
import ru.cft.template.model.response.*;
import ru.cft.template.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/hesoyam")
    public ResponseEntity<WalletShortResponse> hesoyam(Authentication authentication, @RequestBody AmountBody body){
        return ResponseEntity.ok(transactionService.hesoyam(authentication, body));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionHistoryResponse>> getHistory(
            Authentication authentication,
            @RequestParam(required = false) TransactionType type
    ){
        return ResponseEntity.ok(transactionService.getHistory(authentication, type));
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransactionResponse> createTransfer(
            Authentication authentication,
            @RequestBody TransferBody body
    ){
        return ResponseEntity.ok(transactionService.processTransaction(authentication, body));
    }

    @GetMapping("/maintenance")
    public ResponseEntity<List<MaintenanceInfoResponse>> getMaintenances(
            Authentication authentication,
            @RequestParam(required = false) MaintenanceType type
    ){
        return ResponseEntity.ok(transactionService.getUserMaintenances(authentication, type));
    }

    @PostMapping("/maintenance")
    public ResponseEntity<CreatedMaintenanceResponse> createdMaintenance(
            Authentication authentication,
            @RequestBody MaintenanceBody body
    ) {
        return ResponseEntity.ok(transactionService.createMaintenance(authentication, body));
    }
}
