package nl.runnable.gigmatch.commands

import nl.runnable.gigmatch.domain.vacancy.Experience.Level
import nl.runnable.gigmatch.domain.vacancy.Rate
import nl.runnable.gigmatch.domain.vacancy.SkillId
import java.util.*
import nl.runnable.gigmatch.events.RateType as EventRateType

fun RateType.toDomainCounterpart() = when (this) {
    RateType.HOURLY -> Rate.Type.HOURLY
    RateType.DAILY -> Rate.Type.DAILY
    RateType.FIXED -> Rate.Type.FIXED
}

fun RateType.toEventCounterpart() = when (this) {
    RateType.HOURLY -> EventRateType.HOURLY
    RateType.DAILY -> EventRateType.DAILY
    RateType.FIXED -> EventRateType.FIXED
}

fun ExperienceLevel.toDomainCounterpart() = when (this) {
    ExperienceLevel.JUNIOR -> Level.JUNIOR
    ExperienceLevel.MEDIOR -> Level.MEDIOR
    ExperienceLevel.SENIOR -> Level.SENIOR
}

fun Experience.toDomainCounterpart() =
    nl.runnable.gigmatch.domain.vacancy.Experience(SkillId(UUID.fromString(skillId)), level.toDomainCounterpart())
