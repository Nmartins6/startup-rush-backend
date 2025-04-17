package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.BattleEventsDTO;
import dev.nicolas.startuprush.model.EventType;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class BattleService {

    private final StartupBattleRepository battleRepository;
    private final StartupRepository startupRepository;
    private final Random random = new Random();

    public BattleService(StartupBattleRepository battleRepository, StartupRepository startupRepository) {
        this.battleRepository = battleRepository;
        this.startupRepository = startupRepository;
    }

    public StartupBattle applyBattleEvents(BattleEventsDTO dto) {
        StartupBattle battle = battleRepository.findById(dto.getBattleId())
                .orElseThrow(() -> new RuntimeException("Battle not found"));

        Startup a = battle.getStartupA();
        Startup b = battle.getStartupB();

        int scoreA = calculateAndApplyEvents(a, dto.getEventsForStartupA());
        int scoreB = calculateAndApplyEvents(b, dto.getGetEventsForStartupB());

        if (scoreA == scoreB) {
            if (random.nextBoolean()) {
                scoreA += 2;
            } else {
                scoreB += 2;
            }
        }

        Startup winner = scoreA > scoreB ? a : b;
        winner.setScore(winner.getScore() + 30);

        battle.setWinner(winner);
        battle.setCompleted(true);

        startupRepository.save(a);
        startupRepository.save(b);

        return battleRepository.save(battle);
    }

    private int calculateAndApplyEvents(Startup startup, java.util.List<EventType> events) {
        if(events == null) return 0;

        int total = 0;
        for(EventType event : events) {
            total += event.getPoints();
            incrementEventCount(startup, event);
        }

        startup.setScore(startup.getScore() + total);

        return startup.getScore();
    }

    private void incrementEventCount(Startup startup, EventType event) {
        switch (event) {
            case PITCH -> startup.setPitchCount(startup.getPitchCount() +1);
            case BUGS -> startup.setBugsCount(startup.getBugsCount() +1);
            case TRACTION -> startup.setBugsCount(startup.getUserTractionCount() +1);
            case INVESTOR_ANGRY -> startup.setInvestorAngerCount(startup.getInvestorAngerCount() + 1);
            case FAKE_NEWS -> startup.setFakeNewsCount(startup.getFakeNewsCount() +1);
        }
    }

    public StartupBattle createRandomBattle() {
        List<Startup> allStartups = startupRepository.findAll();

        if (allStartups.size() < 4 || allStartups.size() > 8 || allStartups.size() % 2 != 0) {
            throw new IllegalStateException("You must have between 4 and 8 startups, and the total must be even to start the tournament.");
        }

        Collections.shuffle(allStartups);

        StartupBattle battle = StartupBattle.builder()
                .startupA(allStartups.get(0))
                .startupB(allStartups.get(1))
                .completed(false)
                .build();

        return battleRepository.save(battle);
    }

    public List<StartupBattle> getPendingBattles() {
        return battleRepository.findByCompletedFalse();
    }
}
