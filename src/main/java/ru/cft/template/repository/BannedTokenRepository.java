package ru.cft.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.entity.BannedToken;

public interface BannedTokenRepository extends JpaRepository<BannedToken, String> {
}
