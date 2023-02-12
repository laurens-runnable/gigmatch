package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test

class CreateVacancyTest {

    private val fixture = AggregateTestFixture(Vacancy::class.java)

    @Test
    fun `with valid data`() {
        fixture
            .given()
            .`when`(
                CreateVacancy(
                    VacancyId.generateRandom(),
                    testJobTitle(),
                    testStart()
                )
            )
            .expectSuccessfulHandlerExecution()
    }

}
