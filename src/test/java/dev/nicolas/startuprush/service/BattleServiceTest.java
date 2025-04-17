package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.BattleEventsDTO;
import dev.nicolas.startuprush.model.EventType;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BattleServiceTest {

    private StartupBattleRepository battleRepository;
    private StartupRepository startupRepository;
    private BattleService battleService;

    @BeforeEach
    void setUp() {
        battleRepository = mock(StartupBattleRepository.class);
        startupRepository = mock(StartupRepository.class);
        battleService = new BattleService(battleRepository, startupRepository);
    }

    @Test
    void testApplyBattleEventsAndDetermineWinner() {
        Startup a = Startup.builder().id(1L).name("Alpha").score(70).build();
        Startup b = Startup.builder().id(2L).name("Beta").score(70).build();

        StartupBattle battle = StartupBattle.builder()
                .id(1l)
                .startupA(a)
                .startupB(b)
                .completed(false)
                .build();

        BattleEventsDTO dto = new BattleEventsDTO();
        dto.setBattleId(1L);
        dto.setEventsForStartupA(List.of(EventType.PITCH, EventType.TRACTION));
        dto.setGetEventsForStartupB(List.of(EventType.BUGS));

        when(battleRepository.findById(1L)).thenReturn(Optional.of(battle));
        when(startupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(battleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StartupBattle result = battleService.applyBattleEvents(dto);

        assertTrue(result.isCompleted());
        assertNotNull(result.getWinner());
        assertEquals("Alpha", result.getWinner().getName()); //case test == Alpha: +6 +3 = 79 | Beta: -4 = 66
        assertEquals(109, result.getWinner().getScore()); //case test == 79 + 39 bonus
    }
}
