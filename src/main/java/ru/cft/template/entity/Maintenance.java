package ru.cft.template.entity;

import jakarta.persistence.*;
import ru.cft.template.model.MaintenanceStatus;
import ru.cft.template.model.MaintenanceType;

import java.util.Date;
import java.util.UUID;

public class Maintenance {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceType type;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long maintenanceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", referencedColumnName = "id")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id", referencedColumnName = "id")
    private Wallet receiverWallet;
}
