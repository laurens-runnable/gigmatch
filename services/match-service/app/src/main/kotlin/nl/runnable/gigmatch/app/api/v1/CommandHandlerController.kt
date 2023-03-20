package nl.runnable.gigmatch.app.api.v1

import nl.runnable.gigmatch.framework.commands.CommandDeserializationException
import nl.runnable.gigmatch.framework.commands.CommandResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream

@RestController
@RequestMapping("/api/v1/commands", consumes = ["application/avro"])
class CommandHandlerController {

    @Autowired
    private lateinit var commandResolver: CommandResolver

    @PostMapping
    fun handleCommand(input: InputStream, @RequestHeader("X-gm.type") type: String): ResponseEntity<Void> {
        val authentication =
            SecurityContextHolder.getContext().authentication ?: return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)

        if (!commandResolver.isCommandType(type)) {
            return ResponseEntity<Void>(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        }

        val command = commandResolver.deserializeCommand(type, input) //

        val commandHandler =
            commandResolver.resolveCommandHandler(type)
                ?: return ResponseEntity<Void>(HttpStatus.UNSUPPORTED_MEDIA_TYPE)

        if (!commandResolver.isAllowed(authentication, command)) {
            return ResponseEntity<Void>(HttpStatus.FORBIDDEN)
        }

        commandHandler.accept(command)

        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @ExceptionHandler(CommandDeserializationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun badRequest() = Unit

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun unprocessableEntity() = Unit
}
