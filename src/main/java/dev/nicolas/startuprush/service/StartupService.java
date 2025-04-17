package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.StartupReportDTO;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Startup> drawTwoRandomStartups() {
        List<Startup> all = startupRepository.findAll();

        if(all.size() < 2) {
            throw new IllegalStateException("At least two startups are required to start a battle");
        }

        Collections.shuffle(all);
        return all.subList(0,2);
    }

    public List<StartupReportDTO> getRankingReport() {
        return startupRepository.findAll().stream()
                .map(startup -> StartupReportDTO.builder()
                        .name(startup.getName())
                        .slogan(startup.getSlogan())
                        .score(startup.getScore())
                        .pitchCount(startup.getPitchCount())
                        .bugsCount(startup.getBugsCount())
                        .userTractionCount(startup.getUserTractionCount())
                        .investorAngerCount(startup.getInvestorAngerCount())
                        .fakeNewsCount(startup.getFakeNewsCount())
                        .build())
                .sorted(Comparator.comparingInt(StartupReportDTO::getScore).reversed())
                .collect(Collectors.toList());
    }

}
