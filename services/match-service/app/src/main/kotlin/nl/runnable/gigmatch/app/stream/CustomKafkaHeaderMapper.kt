package nl.runnable.gigmatch.app.stream

import org.apache.kafka.common.header.Headers
import org.springframework.kafka.support.DefaultKafkaHeaderMapper
import org.springframework.messaging.MessageHeaders

internal class CustomKafkaHeaderMapper : DefaultKafkaHeaderMapper() {

    override fun fromHeaders(headers: MessageHeaders, target: Headers) {
        super.fromHeaders(headers, target)
        target.remove(JSON_TYPES)
    }

}
