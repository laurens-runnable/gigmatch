package nl.runnable.gigmatch.stream

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.KafkaHeaderMapper

@Configuration
class StreamConfig {

    @Bean("kafkaHeaderMapper")
    fun kafkaHeaderMapper(): KafkaHeaderMapper {
        return CustomKafkaHeaderMapper()
    }

}
