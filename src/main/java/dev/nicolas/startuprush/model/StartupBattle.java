package dev.nicolas.startuprush.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StartupBattle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Startup startupA;

    @ManyToOne
    private Startup startupB;

    @ManyToOne
    private Startup winner;

    private boolean completed = false;
}
