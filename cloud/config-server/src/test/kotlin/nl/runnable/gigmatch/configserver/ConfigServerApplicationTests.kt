package nl.runnable.gigmatch.configserver

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test", "native")
class ConfigServerApplicationTests {

    @Test
    fun `context loads`() = Unit

}
