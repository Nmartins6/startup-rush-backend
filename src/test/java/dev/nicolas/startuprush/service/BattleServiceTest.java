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
    private BattleEventRepository battleEventRepository;

    private BattleService battleService;

    @BeforeEach
    void setUp() {
        startupRepository = mock(StartupRepository.class);
        battleRepository = mock(StartupBattleRepository.class);
        battleEventRepository = mock(BattleEventRepository.class);

        battleService = new BattleService(startupRepository, battleRepository, battleEventRepository);
    }

    @Test
    void testApplyBattleEventsAndDetermineWinner() {
        BattleEventsRequestDTO dto = new BattleEventsRequestDTO();
        dto.setBattleId(1L);

        BattleEventDTO pitch = new BattleEventDTO();
        pitch.setType("PITCH");
        pitch.setPoints(6);

        BattleEventDTO traction = new BattleEventDTO();
        traction.setType("USER_TRACTION");
        traction.setPoints(3);

        BattleEventDTO bugs = new BattleEventDTO();
        bugs.setType("BUG");
        bugs.setPoints(-4);

        dto.setEventsForStartupA(List.of(pitch, traction));
        dto.setEventsForStartupB(List.of(bugs));

        // TODO: Mock battle data, simulate score updates, assert winner and event counts
    }
}
