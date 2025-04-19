
package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.StartupDTO;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StartupServiceTest {

    private StartupRepository startupRepository;
    private StartupBattleRepository battleRepository;
    private BattleEventRepository eventRepository;
    private StartupService startupService;

    @BeforeEach
    void setUp() {
        startupRepository = mock(StartupRepository.class);
        battleRepository = mock(StartupBattleRepository.class);
        eventRepository = mock(BattleEventRepository.class);
        startupService = new StartupService(startupRepository, battleRepository, eventRepository);
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateName() {
        StartupDTO dto = new StartupDTO("Test", "Slogan", 2021);
        when(startupRepository.existsByName("Test")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> startupService.registerStartup(dto));
    }
}
