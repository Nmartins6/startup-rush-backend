package dev.nicolas.startuprush.repository;

import dev.nicolas.startuprush.model.StartupBattle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StartupBattleRepository extends JpaRepository<StartupBattle, Long> {

    List<StartupBattle> findByCompletedFalse();

    @Query("SELECT MAX(b.round) FROM StartupBattle b")
    Optional<Integer> findMaxRound();

    List<StartupBattle> findByRound(int round);
}
