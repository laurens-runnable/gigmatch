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

        val skillSpec = mock<SkillMustExistSpecification> {
            on { isSatisfiedBy(testSkill()) } doReturn true
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
                    testTerm(),
                    testRate(),
                    testDeadline(),
                ),
            )
            .expectSuccessfulHandlerExecution()
    }

    @Test
    fun `with invalid skill`() {
        val invalidSkill = SkillId.generateRandom()
        fixture
            .given()
            .`when`(
                CreateVacancy(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(invalidSkill)),
                    testTerm(),
                    testRate(),
                    testDeadline(),
                ),
            )
            .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun `with invalid deadline`() {
        val invalidDeadline = testTerm().start.plusDays(1)
        fixture
            .given()
            .`when`(
                CreateVacancy(
                    VacancyId.generateRandom(),
                    Job("Test", setOf(testSkill())),
                    testTerm(),
                    testRate(),
                    invalidDeadline,
                ),
            )
            .expectException(IllegalArgumentException::class.java)
    }
}
