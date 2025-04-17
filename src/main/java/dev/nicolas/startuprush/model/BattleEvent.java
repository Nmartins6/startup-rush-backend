package dev.nicolas.startuprush.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BattleEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String type;
    private int points;

    @ManyToOne
    private Startup startup;

    @ManyToOne
    private StartupBattle battle;
}
