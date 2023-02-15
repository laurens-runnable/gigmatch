package nl.runnable.gigmatch.stream

import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.messaging.converter.MessageConverter
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.KafkaHeaderMapper

@Configuration
class StreamConfiguration {

    @Bean("kafkaHeaderMapper")
    fun kafkaHeaderMapper(): KafkaHeaderMapper = CustomKafkaHeaderMapper()

    @Bean
    fun avroSchemaMessageConverter(): MessageConverter = AvroSchemaMessageConverter()

}
