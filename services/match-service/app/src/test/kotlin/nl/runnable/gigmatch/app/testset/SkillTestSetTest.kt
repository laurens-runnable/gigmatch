package nl.runnable.gigmatch.app.testset

import nl.runnable.gigmatch.app.receiveEvent
import nl.runnable.gigmatch.events.SkillCreatedOrUpdated
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class SkillTestSetTest {

    @Autowired
    private lateinit var skillTestSet: SkillTestSet

    @Autowired
    private lateinit var outputDestination: OutputDestination

    @Test
    fun `reset() should generate SkillCreatedOrUpdated events`() {
        skillTestSet.reset()

        val names = listOf("Java", "Kotlin", "Spring", "React", "Selenium")
        for (name in names) {
            val event = outputDestination.receiveEvent(SkillCreatedOrUpdated::class.java, "match-events")
            event.name.shouldBeEqualTo(name)
        }
    }
}
