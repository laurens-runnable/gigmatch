package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.whenever
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class CloseVacancyCommandTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Vacancy::class.java)
        fixture.registerInjectableResource(mock<EventScheduler> {})
    }

    @Test
    fun `should be successful for vacancy that is open`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(CloseVacancyCommand(id))
            .expectSuccessfulHandlerExecution()
    }

    @Test
    fun `should be unsuccessful for vacancy that is closed`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyClosedEvent())
            .whenever(CloseVacancyCommand(id))
            .expectException(IllegalStateException::class.java)
    }

    @Test
    fun `should be unsuccessful for vacancy that is cancelled`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyCancelledEvent())
            .whenever(CloseVacancyCommand(id))
            .expectException(IllegalStateException::class.java)
    }
}
