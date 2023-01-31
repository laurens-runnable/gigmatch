package nl.runnable.gigmatch.framework.commands

import java.io.InputStream
import java.util.function.Consumer

typealias CommandHandler = Consumer<Any>

/**
 * Defines operations for handling commands.
 */
interface CommandResolver {

    /**
     * Tests if the given type represents a command.
     */
    fun isCommandType(type: String): Boolean

    /**
     * Deserializes a command of a given type from an InputStream.
     *
     * @throws IllegalArgumentException If the type does not represent a valid command or the input is malformed.
     * @see [isCommandType]
     */
    fun deserializeCommand(type: String, input: InputStream): Any

    /**
     * Resolves the [CommandHandler] for a given type.
     *
     * @return The CommandHandler or `null` if none could be resolved.
     */
    fun resolveCommandHandler(type: String): CommandHandler?

}
