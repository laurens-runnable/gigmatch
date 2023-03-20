package nl.runnable.gigmatch.events

import nl.runnable.gigmatch.domain.vacancy.Rate
import nl.runnable.gigmatch.domain.vacancy.Experience as DomainExperience
import nl.runnable.gigmatch.events.Experience as FrameworkExperience

fun Rate.Type.toFrameworkCounterpart() = when (this) {
    Rate.Type.HOURLY -> RateType.HOURLY
    Rate.Type.DAILY -> RateType.DAILY
    Rate.Type.FIXED -> RateType.FIXED
}

fun DomainExperience.Level.toFrameworkCounterpart() = when (this) {
    DomainExperience.Level.JUNIOR -> ExperienceLevel.JUNIOR
    DomainExperience.Level.MEDIOR -> ExperienceLevel.MEDIOR
    DomainExperience.Level.SENIOR -> ExperienceLevel.SENIOR
}

fun DomainExperience.toFrameworkCounterpart() = FrameworkExperience(
    skillId.toUUID(),
    level.toFrameworkCounterpart(),
)
