package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CreateVacancyTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    @BeforeEach
    fun setup() {
        fixture = AggregateTestFixture(Vacancy::class.java)

        val skillSpec = mock<SkillSpec> {
            on { skillExists(testSkill()) } doReturn true
        }
        fixture.registerInjectableResource(skillSpec)
    }

    @Test
    fun `with valid data`() {
        fixture
            .given()
            .`when`(
                CreateVacancy(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(testSkill())),
                    testStart(),
                ),
            )
            .expectSuccessfulHandlerExecution()
    }

    @Test
    fun `with invalid skill`() {
        fixture
            .given()
            .`when`(
                CreateVacancy(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(SkillId.generateRandom())),
                    testStart(),
                ),
            )
            .expectException(IllegalArgumentException::class.java)
    }
}
