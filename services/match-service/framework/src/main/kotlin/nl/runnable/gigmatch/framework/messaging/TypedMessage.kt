package nl.runnable.gigmatch.framework.messaging

import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders

class TypedMessage<T : Any>(private val event: T) : Message<T> {

    companion object {
        const val TYPE_HEADER = "gm.type"
    }

    override fun getPayload(): T = event

    override fun getHeaders() = MessageHeaders(mapOf(TYPE_HEADER to event::class.qualifiedName))
}
