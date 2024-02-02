package ru.cft.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.entity.BannedToken;

import java.util.Optional;

public interface BannedTokenRepository extends JpaRepository<BannedToken, String> {
    Optional<BannedToken> findByToken(String token);
}
