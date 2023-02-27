package nl.runnable.gigmatch.app.stream

import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaMessageConverter
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaServiceManagerImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.KafkaHeaderMapper
import org.springframework.messaging.converter.MessageConverter

@Configuration
class StreamConfiguration {

    @Bean("kafkaHeaderMapper")
    fun kafkaHeaderMapper(): KafkaHeaderMapper = CustomKafkaHeaderMapper()

    @Bean
    fun avroSchemaMessageConverter(): MessageConverter = AvroSchemaMessageConverter(AvroSchemaServiceManagerImpl())
}
