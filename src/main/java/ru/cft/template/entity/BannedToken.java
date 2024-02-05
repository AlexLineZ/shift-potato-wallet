package ru.cft.template.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "banned_tokens")
@Data
public class BannedToken {
    @Id
    private String token;
}


