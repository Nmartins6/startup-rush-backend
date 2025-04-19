package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.StartupDTO;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class StartupServiceTest {

    private StartupRepository startupRepository;
    private StartupBattleRepository battleRepository;
    private BattleEventRepository battleEventRepository;
    private StartupService startupService;

    @BeforeEach
    void setUp() {
        startupRepository = mock(StartupRepository.class);
        battleRepository = mock(StartupBattleRepository.class);
        battleEventRepository = mock(BattleEventRepository.class);

        startupService = new StartupService(startupRepository, battleRepository, battleEventRepository);
    }

    @Test
    void testRegisterStartupWithinLimit() {
        StartupDTO dto = new StartupDTO();
        dto.setName("TestName");
        dto.setSlogan("TestSlogan");
        dto.setFoundationYear(2020);

        when(startupRepository.count()).thenReturn(4L);
        when(startupRepository.existsByName("TestName")).thenReturn(false);

        Startup expectedStartup = Startup.builder()
                .name("TestName")
                .slogan("TestSlogan")
                .foundationYear(2020)
                .score(70)
                .build();

        when(startupRepository.save(any(Startup.class))).thenReturn(expectedStartup);

        Startup saved = startupService.registerStartup(dto);

        assertEquals("TestName", saved.getName());
        assertEquals("TestSlogan", saved.getSlogan());
        assertEquals(2020, saved.getFoundationYear());

        verify(startupRepository).save(any(Startup.class));
    }
}
