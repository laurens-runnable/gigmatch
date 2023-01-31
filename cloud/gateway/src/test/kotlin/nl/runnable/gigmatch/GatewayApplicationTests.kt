package nl.runnable.gigmatch

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Gateway")
@ActiveProfiles("test")
class GatewayApplicationTests {

    @Test
    fun `context loads`() = Unit

}
