package nl.runnable.gigmatch.app

import nl.runnable.gigmatch.app.api.v1.SkillImporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestSetConfiguration {

    @Bean
    fun skillTestSet() = SkillImporter()
}
