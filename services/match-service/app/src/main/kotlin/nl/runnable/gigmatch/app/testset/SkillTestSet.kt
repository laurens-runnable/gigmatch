package nl.runnable.gigmatch.app.testset

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import nl.runnable.gigmatch.framework.genre.GenreEntityRepository
import nl.runnable.gigmatch.framework.genre.SkillEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.util.*

@Component
@TestSetProfile
class SkillTestSet {

    @Autowired
    private lateinit var repository: GenreEntityRepository

    @Value("classpath:/test-set/skills.csv")
    private lateinit var resource: Resource

    fun reset() {
        val rows = csvReader().readAllWithHeader(resource.inputStream)
        val genres = rows.map {
            val skillEntity = SkillEntity(UUID.fromString(it["id"] as String))
            skillEntity.name = it["name"] as String
            skillEntity.slug = it["slug"] as String
            skillEntity
        }

        repository.deleteAll()
        repository.saveAll(genres)
    }

}
