package nl.runnable.gigmatch.app

import nl.runnable.gigmatch.app.testset.SkillTestSet
import nl.runnable.gigmatch.app.testset.VacancyTestSet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestSetConfiguration {

    @Bean
    fun skillTestSet() = SkillTestSet()

    @Bean
    fun vacancyTestSet() = VacancyTestSet()
}
