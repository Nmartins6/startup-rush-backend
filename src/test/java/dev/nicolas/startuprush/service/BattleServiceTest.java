package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.BattleEventDTO;
import dev.nicolas.startuprush.dto.BattleEventsRequestDTO;
import dev.nicolas.startuprush.model.BattleEvent;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BattleServiceTest {

    private StartupRepository startupRepository;
    private StartupBattleRepository battleRepository;
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
    void testApplyBattleEventsWithValidEventTypes() {
        BattleEventsRequestDTO request = new BattleEventsRequestDTO();
        request.setBattleId(1L);

        request.setEventsForStartupA(List.of(new BattleEventDTO("PITCH")));
        request.setEventsForStartupB(List.of(new BattleEventDTO("BUG")));

        Startup startupA = Startup.builder().id(1L).name("A").score(70).build();
        Startup startupB = Startup.builder().id(2L).name("B").score(70).build();
        StartupBattle battle = StartupBattle.builder()
                .id(1L)
                .startupA(startupA)
                .startupB(startupB)
                .completed(false)
                .round(0)
                .build();

        when(battleRepository.findById(1L)).thenReturn(Optional.of(battle));
        when(battleEventRepository.existsByStartupIdAndBattleIdAndType(anyLong(), anyLong(), anyString())).thenReturn(false);
        when(battleRepository.findAll()).thenReturn(List.of(battle));
        when(battleRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> battleService.applyBattleEvents(request));
    }

    @Test
    void testApplyBattleEventsWithInvalidEventType() {
        BattleEventsRequestDTO request = new BattleEventsRequestDTO();
        request.setBattleId(1L);
        request.setEventsForStartupA(List.of(new BattleEventDTO("CAFÉ_GRÁTIS"))); // evento inválido

        Startup startupA = Startup.builder().id(1L).name("A").score(70).build();
        StartupBattle battle = StartupBattle.builder()
                .id(1L)
                .startupA(startupA)
                .startupB(startupA)
                .completed(false)
                .round(0)
                .build();

        when(battleRepository.findById(1L)).thenReturn(Optional.of(battle));
        when(battleEventRepository.existsByStartupIdAndBattleIdAndType(anyLong(), anyLong(), anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                battleService.applyBattleEvents(request));
        assertTrue(exception.getMessage().contains("Unknown event type"));
    }

    @Test
    void shouldStartNextRoundWhenAllBattlesAreCompleted() {
        Startup startup1 = Startup.builder().id(1L).name("Alpha").score(100).build();
        Startup startup2 = Startup.builder().id(2L).name("Beta").score(90).build();
        Startup startup3 = Startup.builder().id(3L).name("Gamma").score(95).build();
        Startup startup4 = Startup.builder().id(4L).name("Delta").score(92).build();

        StartupBattle battle1 = StartupBattle.builder()
                .id(1L)
                .startupA(startup1)
                .startupB(startup2)
                .winner(startup1)
                .round(0)
                .completed(true)
                .build();

        StartupBattle battle2 = StartupBattle.builder()
                .id(2L)
                .startupA(startup3)
                .startupB(startup4)
                .winner(startup3)
                .round(0)
                .completed(true)
                .build();

        when(battleRepository.findAll()).thenReturn(new ArrayList<>(List.of(battle1, battle2)));
        when(battleRepository.save(any(StartupBattle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<StartupBattle> newBattles = battleService.startNextRound();

        assertNotNull(newBattles);
        assertEquals(1, newBattles.size());
        verify(battleRepository, atLeastOnce()).save(any(StartupBattle.class));
    }
}
