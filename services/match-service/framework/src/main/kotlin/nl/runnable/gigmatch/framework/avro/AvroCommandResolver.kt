package nl.runnable.gigmatch.framework.avro

import jakarta.annotation.security.RolesAllowed
import nl.runnable.gigmatch.framework.COMMAND_HANDLER_SUFFIX
import nl.runnable.gigmatch.framework.commands.CommandDeserializationException
import nl.runnable.gigmatch.framework.commands.CommandHandler
import nl.runnable.gigmatch.framework.commands.CommandResolver
import org.apache.avro.io.DecoderFactory
import org.apache.avro.reflect.ReflectDatumReader
import org.apache.avro.specific.SpecificRecord
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils
import java.io.InputStream
import java.util.function.Consumer

/**
 * Avro-based [CommandResolver] implementation.
 *
 * This implementation uses [RolesAllowed] annotations on the Command classes and resolves [CommandHandler]
 * beans from the ApplicationContext.
 */
@Component
internal class AvroCommandResolver : CommandResolver {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    override fun isCommandType(type: String): Boolean =
        try {
            val clazz = Class.forName(type)
            clazz.packageName == "nl.runnable.gigmatch.commands" && ClassUtils.isAssignable(
                SpecificRecord::class.java,
                clazz,
            )
        } catch (e: ClassNotFoundException) {
            false
        }

    override fun deserializeCommand(type: String, input: InputStream): Any {
        try {
            require(isCommandType(type)) { "$type is not a valid command type" }

            val clazz = Class.forName(type)
            val reader = ReflectDatumReader(clazz)
            val binaryDecoder = DecoderFactory.get().binaryDecoder(input, null)
            return reader.read(null, binaryDecoder)
        } catch (e: IllegalArgumentException) {
            throw CommandDeserializationException("Error deserializing command: $e.message")
        }
    }

    override fun isAllowed(authentication: Authentication, command: Any): Boolean {
        val rolesAllowed = command.javaClass.getAnnotation(RolesAllowed::class.java) ?: return false
        return authentication.authorities.any { rolesAllowed.value.contains(it.toString()) }
    }

    override fun resolveCommandHandler(type: String): CommandHandler? =
        try {
            @Suppress("UNCHECKED_CAST")
            applicationContext.getBean("$type$COMMAND_HANDLER_SUFFIX", Consumer::class.java) as CommandHandler
        } catch (e: BeansException) {
            null
        }
}
