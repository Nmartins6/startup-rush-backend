package dev.nicolas.startuprush.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StartupDTO {
    private String name;
    private String slogan;
    private String foundationYear;

}
