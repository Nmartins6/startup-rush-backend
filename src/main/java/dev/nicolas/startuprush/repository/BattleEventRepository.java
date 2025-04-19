package dev.nicolas.startuprush.repository;

import dev.nicolas.startuprush.model.BattleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleEventRepository extends JpaRepository<BattleEvent, Long> {
    List<BattleEvent> findByStartupId(Long startupId);
    List<BattleEvent> findByBattleId(Long battleId);
    boolean existsByStartupIdAndBattleIdAndType(Long startupId, Long battleId, String type);
}
