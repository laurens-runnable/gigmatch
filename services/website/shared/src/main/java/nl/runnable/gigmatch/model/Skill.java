package nl.runnable.gigmatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private UUID uuid;

    private String name;

    private String slug;
}
