package nl.runnable.gigmatch.events

import nl.runnable.gigmatch.domain.vacancy.Experience
import nl.runnable.gigmatch.domain.vacancy.Rate

fun Rate.Type.toFrameworkCounterpart() = when (this) {
    Rate.Type.HOURLY -> RateType.HOURLY
    Rate.Type.DAILY -> RateType.DAILY
    Rate.Type.FIXED -> RateType.FIXED
}

fun Experience.Level.toFrameworkCounterpart() = when (this) {
    Experience.Level.JUNIOR -> ExperienceLevel.JUNIOR
    Experience.Level.MEDIOR -> ExperienceLevel.MEDIOR
    Experience.Level.SENIOR -> ExperienceLevel.SENIOR
}

fun Experience.toFrameworkCounterpart() = Experience(
    skillId.toString(),
    level.toFrameworkCounterpart(),
)
