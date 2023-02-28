package nl.runnable.gigmatch.app.testset

import nl.runnable.gigmatch.app.receiveEvent
import nl.runnable.gigmatch.events.RateType
import nl.runnable.gigmatch.events.VacancyCreated
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class VacancyTestSetTest {

    @Autowired
    private lateinit var skillTestSet: SkillTestSet

    @Autowired
    private lateinit var vacancyTestSet: VacancyTestSet

    @Autowired
    private lateinit var outputDestination: OutputDestination

    @Test
    fun `reset() should generate VacancyCreated event`() {
        skillTestSet.reset()
        vacancyTestSet.reset()

        val event = outputDestination.receiveEvent(VacancyCreated::class.java, "match-events")
        event.id.shouldBeEqualTo(UUID.fromString("d3a02f50-e0be-4bbd-bc82-eec4ca8453a4"))
        event.jobTitle.shouldBeEqualTo("Java developer")
        event.skills.size.shouldBeEqualTo(1)
        event.skills.shouldContain("4aabe43b-aae0-4af4-8ccc-29d060fa94ba")
        event.rateAmount.shouldBeEqualTo(100)
        event.rateType.shouldBeEqualTo(RateType.HOURLY)
    }
}
