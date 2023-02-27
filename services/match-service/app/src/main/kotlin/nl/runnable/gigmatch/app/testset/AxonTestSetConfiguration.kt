package nl.runnable.gigmatch.app.testset

import org.axonframework.config.ConfigurerModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@TestSetProfile
class AxonTestSetConfiguration {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AxonTestSetConfiguration::class.java)
    }

    @Bean
    fun processorDefaultConfigurerModule(): ConfigurerModule {
        logger.info("Using Axon subscribing event processor for 'test-set' profile")
        return ConfigurerModule { it ->
            it.eventProcessing { it.usingSubscribingEventProcessors() }
        }
    }
}
