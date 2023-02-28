package nl.runnable.gigmatch.app

import org.axonframework.config.ConfigurerModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonTestConfiguration {
    @Bean
    fun processorDefaultConfigurerModule(): ConfigurerModule {
        return ConfigurerModule { it ->
            it.eventProcessing { it.usingSubscribingEventProcessors() }
        }
    }
}
