package nl.runnable.gigmatch.app.api.v1

import nl.runnable.gigmatch.app.AVRO_MEDIA_TYPE
import nl.runnable.gigmatch.app.receiveEvent
import nl.runnable.gigmatch.app.testset.SkillTestSet
import nl.runnable.gigmatch.app.toByteArray
import nl.runnable.gigmatch.commands.OpenVacancy
import nl.runnable.gigmatch.commands.RateType
import nl.runnable.gigmatch.commands.TestCommand
import nl.runnable.gigmatch.commands.toEventCounterpart
import nl.runnable.gigmatch.events.VacancyOpened
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.OutputDestination
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
    companion object {

        private fun testVacancyId(): UUID? = UUID.randomUUID()

        private fun testJobTitle() = "Test engineer"

        private fun testSkillId() = UUID.fromString("2baaef78-eb31-4f82-a1c4-883bda3a50ff")

        private fun testStart() = LocalDate.now().plusMonths(2).withDayOfMonth(1)

        private fun testEnd() = testStart().plusMonths(6)

        private fun testRateAmount() = 100

        private fun testRateType() = RateType.HOURLY

        private fun testDeadline() = testStart().minusWeeks(2)

        private fun testOpenVacancy(): OpenVacancy {
            return OpenVacancy(
                testVacancyId(),
                testJobTitle(),
                testSkillId(),
                testStart(),
                testEnd(),
                testRateAmount(),
                testRateType(),
                testDeadline(),
            )
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var skillTestSet: SkillTestSet

    @Autowired
    private lateinit var outputDestination: OutputDestination

    @BeforeEach
    fun resetTestSets() {
        skillTestSet.reset()
    }

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
    fun `should return 202 Accepted and result in VacancyOpened event`() {
        val vacancyId = testVacancyId()
        val jobTitle = testJobTitle()
        val skillId = testSkillId()
        val start = testStart()
        val end = testEnd()
        val rateAmount = testRateAmount()
        val rateType = testRateType()
        val deadline = testDeadline()

        useRecruiterAuthentication()

        mockMvc.post("/api/v1/commands") {
            val command = OpenVacancy(vacancyId, jobTitle, skillId, start, end, rateAmount, rateType, deadline)
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isAccepted() }
        }

        val event = outputDestination.receiveEvent(VacancyOpened::class.java, "match-events")
        event.id.shouldBeEqualTo(vacancyId)
        event.jobTitle.shouldBeEqualTo(jobTitle)
        event.skills.size.shouldBeEqualTo(1)
        event.skills.shouldContain(skillId.toString()) // List contains String instance, rather than UUID
        event.start.shouldBeEqualTo(start)
        event.end.shouldBeEqualTo(end)
        event.rateAmount.shouldBeEqualTo(rateAmount)
        event.rateType.shouldBeEqualTo(rateType.toEventCounterpart())
        event.deadline.shouldBeEqualTo(deadline)
    }

    @Test
    fun `should return 422 Unprocessable Entity for invalid commands`() {
        useRecruiterAuthentication()

        mockMvc.post("/api/v1/commands") {
            val invalidSkillId = UUID.randomUUID()
            val command =
                OpenVacancy(
                    testVacancyId(),
                    testJobTitle(),
                    invalidSkillId,
                    testStart(),
                    testEnd(),
                    testRateAmount(),
                    testRateType(),
                    testDeadline(),
                )
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `should return 401 Unauthorized if not authenticated`() {
        useNoAuthentication()

        mockMvc.post("/api/v1/commands") {
            val command = testOpenVacancy()
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
            val command = testOpenVacancy()
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun `should return 415 Unsupported Media Type for unknown command types`() {
        useRecruiterAuthentication()

        mockMvc.post("/api/v1/commands") {
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", "unknown.type")
        }.andExpect {
            status { isUnsupportedMediaType() }
        }
    }

    @Test
    fun `should return 415 Unsupported Media Type for unhandled commands`() {
        useRecruiterAuthentication()

        mockMvc.post("/api/v1/commands") {
            val command = TestCommand()
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", command.javaClass.name)
            content = command.toByteArray()
        }.andExpect {
            status { isUnsupportedMediaType() }
        }
    }

    @Test
    fun `should return 400 Bad Request for malformed commands`() {
        useRecruiterAuthentication()

        mockMvc.post("/api/v1/commands") {
            contentType = AVRO_MEDIA_TYPE
            header("X-gm.type", OpenVacancy::class.java.name)
            content = ByteArray(4)
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
