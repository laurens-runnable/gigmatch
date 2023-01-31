package nl.runnable.gigmatch.api

import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.avro.specific.SpecificRecord
import java.io.ByteArrayOutputStream

fun SpecificRecord.toByteArray(): ByteArray {
    val output = ByteArrayOutputStream()
    val encoder = EncoderFactory.get().binaryEncoder(output, null)
    val writer = SpecificDatumWriter(javaClass)
    writer.write(this, encoder)
    encoder.flush()
    return output.toByteArray()
}
