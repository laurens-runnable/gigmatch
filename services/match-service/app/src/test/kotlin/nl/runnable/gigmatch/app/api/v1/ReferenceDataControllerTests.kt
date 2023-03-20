package nl.runnable.gigmatch.app.api.v1

import nl.runnable.gigmatch.framework.vacancy.SkillEntityRepository
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security filter
@ActiveProfiles("test")
class ReferenceDataControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Value("classpath:/skills.csv")
    private lateinit var skillsCsv: Resource

    @Autowired
    private lateinit var skillEntityRepository: SkillEntityRepository

    @Test
    fun `should return 200 OK and populate skills`() {
        skillEntityRepository.deleteAll()

        mockMvc.post("/api/v1/reference-data/skills") {
            contentType = MediaType.valueOf("text/csv")
            content = skillsCsv.contentAsByteArray
        }.andExpect {
            status { isNoContent() }
        }

        skillEntityRepository.count() shouldBeEqualTo 5
    }
}
