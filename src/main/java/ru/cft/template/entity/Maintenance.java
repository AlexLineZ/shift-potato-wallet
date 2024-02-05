package ru.cft.template.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.cft.template.model.MaintenanceStatus;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "maintenances")
@Data
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", referencedColumnName = "id")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id", referencedColumnName = "id")
    private Wallet receiverWallet;
}
