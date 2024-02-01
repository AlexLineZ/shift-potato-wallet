package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.request.MaintenanceBody;
import ru.cft.template.model.request.TransferBody;
import ru.cft.template.model.response.CreatedMaintenanceResponse;
import ru.cft.template.model.response.MaintenanceInfoResponse;
import ru.cft.template.service.impl.TransactionService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/history")
    public String getHistory(){
        return "Ok";
    }

    @PostMapping("/transfers")
    public ResponseEntity<?> createTransfer(Authentication authentication, @RequestBody TransferBody body){
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
