package nl.runnable.gigmatch.app.api.v1

import nl.runnable.gigmatch.framework.commands.CommandResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.io.InputStream

@RestController
class CommandHandlerController {

    @Autowired
    private lateinit var commandResolver: CommandResolver

    @PostMapping("/api/v1/commands", consumes = ["application/avro"])
    fun handleCommand(input: InputStream, @RequestHeader("X-gm.type") type: String): ResponseEntity<Void> {
        val authentication =
            SecurityContextHolder.getContext().authentication ?: return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)

        if (!commandResolver.isCommandType(type)) {
            return ResponseEntity<Void>(HttpStatus.BAD_REQUEST)
        }

        val command = commandResolver.deserializeCommand(type, input)
        if (!commandResolver.isAllowed(authentication, command)) {
            return ResponseEntity<Void>(HttpStatus.FORBIDDEN)
        }

        val commandHandler =
            commandResolver.resolveCommandHandler(type) ?: return ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY)
        commandHandler.accept(command)

        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun badRequest() = Unit

}
