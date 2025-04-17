package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartupService {
    private final StartupRepository startupRepository;


    public StartupService(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    public Startup registerStartup(Startup startup) {
        long count = startupRepository.count();
        if (count >= 8) {
            throw new IllegalStateException("Cannot register more than 8 startups");
        }
        return startupRepository.save(startup);
    }

    public boolean isValidStartupCount() {
        long count = startupRepository.count();
        return count >= 4 && count <= 8 && count%2 == 0;
    }

    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    public void clearAllStartups() {
        startupRepository.deleteAll();
    }

}
