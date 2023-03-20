package nl.runnable.gigmatch.app.api.v1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream
import java.io.InputStreamReader

@RestController
@RequestMapping("/api/v1/reference-data", consumes = ["text/csv"])
class ReferenceDataController {
    @Autowired
    private lateinit var skillImporter: SkillImporter

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ReferenceDataController::class.java)
    }

    @PostMapping("/skills")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun importSkills(input: InputStream) {
        logger.info("Importing skills")

        val reader = InputStreamReader(input)
        skillImporter.importFromCsv(reader)
    }
}
