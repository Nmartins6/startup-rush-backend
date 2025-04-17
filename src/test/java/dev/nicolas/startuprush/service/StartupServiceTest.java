package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class StartupServiceTest {

    private StartupRepository startupRepository;
    private StartupService startupService;

    @BeforeEach
    void setUp() {
        startupRepository = mock(StartupRepository.class);
        startupService = new StartupService(startupRepository);
    }

    @Test
    void testRegisterStartupWithinLimit() {
        Startup startup = new Startup("TestName", "TestSlogan", 2020);
        when(startupRepository.count()).thenReturn(4L);
        when(startupRepository.save(startup)).thenReturn(startup);

        Startup saved = startupService.registerStartup(startup);

        assertEquals("TestName", saved.getName());
        verify(startupRepository).save(startup);
    }
}