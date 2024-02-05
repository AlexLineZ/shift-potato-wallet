package ru.cft.template.service;

import org.springframework.security.core.Authentication;
import ru.cft.template.model.MaintenanceType;
import ru.cft.template.model.TransactionType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.MaintenanceBody;
import ru.cft.template.model.request.TransferBody;
import ru.cft.template.model.response.*;

import java.util.List;

public interface TransactionService {
    WalletShortResponse hesoyam(Authentication authentication, AmountBody body);

    List<TransactionHistoryResponse> getHistory(Authentication authentication, TransactionType type);

    TransactionResponse processTransaction(Authentication authentication, TransferBody request);

    CreatedMaintenanceResponse createMaintenance(Authentication authentication, MaintenanceBody body);

    List<MaintenanceInfoResponse> getUserMaintenances(Authentication authentication, MaintenanceType type);
}
