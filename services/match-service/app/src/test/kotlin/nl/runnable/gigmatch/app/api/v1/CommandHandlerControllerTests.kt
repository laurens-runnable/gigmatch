package nl.runnable.gigmatch.app.api.v1

import nl.runnable.gigmatch.api.AVRO_MEDIA_TYPE
import nl.runnable.gigmatch.api.toByteArray
import nl.runnable.gigmatch.commands.CreateVacancy
import nl.runnable.gigmatch.commands.TestCommand
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security filter
@ActiveProfiles("test")
class CommandHandlerControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should return 202 Accepted`() {
        mockMvc.post("/api/v1/commands") {
            val command = CreateVacancy(UUID.randomUUID(), "Test engineer", LocalDate.now().plusMonths(1))
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isAccepted() }
        }
    }

    @Test
    fun `should return 400 Bad Request for unknown command types`() {
        mockMvc.post("/api/v1/commands") {
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", "unknown.type")
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should return 400 Bad Request for malformed commands`() {
        mockMvc.post("/api/v1/commands") {
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", CreateVacancy::class.java.name)
            content = ByteArray(4)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should return 422 Unprocessable Entity for unhandled commands`() {
        mockMvc.post("/api/v1/commands") {
            val command = TestCommand()
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isUnprocessableEntity() }
        }
    }

}
