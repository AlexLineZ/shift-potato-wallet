package ru.cft.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.entity.Maintenance;

import java.util.UUID;

public interface MaintenanceRepository extends JpaRepository<Maintenance, UUID> { }
