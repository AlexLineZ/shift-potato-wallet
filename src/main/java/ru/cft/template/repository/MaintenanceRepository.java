package ru.cft.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.entity.Maintenance;
import ru.cft.template.entity.Wallet;

import java.util.List;
import java.util.UUID;

public interface MaintenanceRepository extends JpaRepository<Maintenance, UUID> {
    List<Maintenance> findBySenderWallet(Wallet wallet);
    List<Maintenance> findByReceiverWallet(Wallet wallet);
    List<Maintenance> findBySenderWalletOrReceiverWallet(Wallet senderWallet, Wallet receiverWallet);
}
