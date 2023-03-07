package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.whenever
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import java.time.Instant
import java.time.LocalDate

class OpenVacancyCommandTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    private lateinit var eventScheduler: EventScheduler

    @BeforeEach
    fun setup() {
        fixture = AggregateTestFixture(Vacancy::class.java)

        val skillSpec = mock<SkillMustExistSpecification> {
            on { isSatisfiedBy(testSkill()) } doReturn true
        }
        fixture.registerInjectableResource(skillSpec)

        eventScheduler = mock {}
        fixture.registerInjectableResource(eventScheduler)
    }

    @Test
    fun `is successful with valid data`() {
        fixture
            .given()
            .whenever(
                OpenVacancyCommand(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(testSkill())),
                    testTerm(),
                    testRate(),
                    testDeadline(),
                ),
            )
            .expectSuccessfulHandlerExecution()

        verify(eventScheduler).schedule(any<Instant>(), any())
        verifyNoMoreInteractions(eventScheduler)
    }

    @Test
    fun `is not successful with invalid skill`() {
        val invalidSkill = SkillId.generateRandom()
        fixture
            .given()
            .whenever(
                OpenVacancyCommand(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(invalidSkill)),
                    testTerm(),
                    testRate(),
                    testDeadline(),
                ),
            )
            .expectException(IllegalArgumentException::class.java)
        verifyNoInteractions(eventScheduler)
    }

    @Test
    fun `is not successful with deadline not after today`() {
        val invalidDeadline = LocalDate.now()
        fixture
            .given()
            .whenever(
                OpenVacancyCommand(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(testSkill())),
                    testTerm(),
                    testRate(),
                    invalidDeadline,
                ),
            )
            .expectException(IllegalArgumentException::class.java)
        verifyNoInteractions(eventScheduler)
    }

    @Test
    fun `is not successful with deadline before job start`() {
        val invalidDeadline = testTerm().start.plusDays(1)
        fixture
            .given()
            .whenever(
                OpenVacancyCommand(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(testSkill())),
                    testTerm(),
                    testRate(),
                    invalidDeadline,
                ),
            )
            .expectException(IllegalArgumentException::class.java)
        verifyNoInteractions(eventScheduler)
    }
}
