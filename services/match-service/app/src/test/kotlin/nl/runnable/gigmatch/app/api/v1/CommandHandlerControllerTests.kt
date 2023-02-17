package nl.runnable.gigmatch.app.api.v1

import nl.runnable.gigmatch.api.AVRO_MEDIA_TYPE
import nl.runnable.gigmatch.api.toByteArray
import nl.runnable.gigmatch.commands.CreateVacancy
import nl.runnable.gigmatch.commands.TestCommand
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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

    private fun useRecruiterAuthentication() {
        SecurityContextHolder.getContext().authentication =
            TestingAuthenticationToken("recruiter1", "recruiter1", "recruiter")
    }

    private fun useCandidateAuthentication() {
        SecurityContextHolder.getContext().authentication =
            TestingAuthenticationToken("candidate1", "candidate1", "candidate")
    }

    private fun useNoAuthentication() {
        SecurityContextHolder.getContext().authentication = null
    }

    @Test
    fun `should return 202 Accepted`() {
        useRecruiterAuthentication()

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
    fun `should return 401 Unauthorized if not authenticated`() {
        useNoAuthentication()

        mockMvc.post("/api/v1/commands") {
            val command = CreateVacancy(UUID.randomUUID(), "Test engineer", LocalDate.now().plusMonths(1))
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    fun `should return 403 Forbidden if authentication does not have allowed role`() {
        useCandidateAuthentication()

        mockMvc.post("/api/v1/commands") {
            val command = CreateVacancy(UUID.randomUUID(), "Test engineer", LocalDate.now().plusMonths(1))
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun `should return 400 Bad Request for unknown command types`() {
        useRecruiterAuthentication()

        mockMvc.post("/api/v1/commands") {
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", "unknown.type")
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should return 400 Bad Request for malformed commands`() {
        useRecruiterAuthentication()

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
        useRecruiterAuthentication()

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
