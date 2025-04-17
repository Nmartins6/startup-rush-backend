package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.BattleEventDTO;
import dev.nicolas.startuprush.dto.BattleEventsRequestDTO;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class BattleServiceTest {

    private StartupBattleRepository battleRepository;
    private StartupRepository startupRepository;
    private BattleService battleService;

    @BeforeEach
    void setUp() {
        battleRepository = mock(StartupBattleRepository.class);
        startupRepository = mock(StartupRepository.class);
        BattleEventRepository battleEventRepository = mock(BattleEventRepository.class);

        BattleService battleService = new BattleService(startupRepository, battleRepository, battleEventRepository);
    }

    @Test
    void testApplyBattleEventsAndDetermineWinner() {
        BattleEventsRequestDTO dto = new BattleEventsRequestDTO();
        dto.setBattleId(1L);

        BattleEventDTO pitch = new BattleEventDTO();
        pitch.setType("PITCH");
        pitch.setPoints(6);

        BattleEventDTO traction = new BattleEventDTO();
        traction.setType("TRACTION");
        traction.setPoints(3);

        BattleEventDTO bugs = new BattleEventDTO();
        bugs.setType("BUGS");
        bugs.setPoints(-5);

        dto.setEventsForStartupA(List.of(pitch, traction));
        dto.setEventsForStartupB(List.of(bugs));
    }
}
