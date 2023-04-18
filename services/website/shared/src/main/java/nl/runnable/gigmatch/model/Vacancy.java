package nl.runnable.gigmatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vacancy {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExperienceItem {
        private Skill skill;

        private int level;
    }

    private UUID id;

    private String jobTitle;

    private List<ExperienceItem> experience = Collections.emptyList();

}
