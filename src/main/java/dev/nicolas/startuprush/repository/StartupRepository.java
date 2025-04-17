package dev.nicolas.startuprush.repository;

import dev.nicolas.startuprush.model.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartupRepository extends JpaRepository<Startup, Long> {
}
