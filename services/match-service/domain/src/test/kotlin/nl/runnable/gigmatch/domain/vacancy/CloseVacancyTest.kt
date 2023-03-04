package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.whenever
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class CloseVacancyTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Vacancy::class.java)
        fixture.registerInjectableResource(mock<EventScheduler> {})
    }

    @Test
    fun `for vacancy that is open`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(CloseVacancy(id))
            .expectSuccessfulHandlerExecution()
    }

    @Test
    fun `for vacancy that is closed`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyClosed())
            .whenever(CloseVacancy(id))
            .expectException(IllegalStateException::class.java)
    }

    @Test
    fun `for vacancy that is cancelled`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyCancelled())
            .whenever(CloseVacancy(id))
            .expectException(IllegalStateException::class.java)
    }
}
