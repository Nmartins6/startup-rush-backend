package dev.nicolas.startuprush.repository;

import dev.nicolas.startuprush.model.StartupBattle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StartupBattleRepository extends JpaRepository<StartupBattle, Long> {

    List<StartupBattle> findByCompletedFalse();

}
