package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CloseVacancyTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    private lateinit var id: VacancyId

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Vacancy::class.java)
        id = VacancyId.generateRandom()
    }

    @Test
    fun `for vacancy that is open`() {
        fixture
            .given(testVacancyCreated(id))
            .`when`(CloseVacancy(id))
            .expectSuccessfulHandlerExecution()
    }

    @Test
    fun `for vacancy that is closed`() {
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyClosed(id))
            .`when`(CloseVacancy(id))
            .expectException(IllegalStateException::class.java)
    }

    @Test
    fun `for vacancy that is cancelled`() {
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyCancelled(id))
            .`when`(CloseVacancy(id))
            .expectException(IllegalStateException::class.java)
    }

}