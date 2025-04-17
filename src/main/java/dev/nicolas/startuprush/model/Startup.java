package dev.nicolas.startuprush.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Startup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String slogan;
    private int foundationYear;

    private int score = 70;

    private int pitchCount = 0;
    private int bugsCount = 0;
    private int userTractionCount = 0;
    private int investorAngerCount = 0;
    private int fakeNewsCount = 0;

    public Startup(String name, String slogan, int foundationYear) {
        this.name = name;
        this.slogan = slogan;
        this.foundationYear = foundationYear;
    }

}
