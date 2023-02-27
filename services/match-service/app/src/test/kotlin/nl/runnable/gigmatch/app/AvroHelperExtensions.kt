package nl.runnable.gigmatch.app

import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.avro.reflect.ReflectDatumReader
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.avro.specific.SpecificRecord
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.messaging.Message
import java.io.ByteArrayOutputStream

fun SpecificRecord.toByteArray(): ByteArray {
    val output = ByteArrayOutputStream()
    val encoder = EncoderFactory.get().binaryEncoder(output, null)
    val writer = SpecificDatumWriter(javaClass)
    writer.write(this, encoder)
    encoder.flush()
    return output.toByteArray()
}

fun <T> OutputDestination.receiveEvent(
    clazz: Class<T>,
    bindingName: String,
    maxEvents: Int = 100,
    timeout: Long = 100,
): T {
    val type = clazz.name
    for (i in 1..maxEvents) {
        val message: Message<ByteArray>? = receive(timeout, bindingName)
        if (message != null && message.headers["gm.type"] == type) {
            val reader = ReflectDatumReader(clazz)
            val binaryDecoder = DecoderFactory.get().binaryDecoder(message.payload, null)
            return reader.read(null, binaryDecoder)
        }
    }
    throw NoSuchElementException("No message received of type '$type'")
}
