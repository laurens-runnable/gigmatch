package nl.runnable.gigmatch.app.testset

import com.opencsv.bean.CsvToBeanBuilder
import nl.runnable.gigmatch.framework.vacancy.SkillEntity
import nl.runnable.gigmatch.framework.vacancy.SkillEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.util.*

@Component
@TestSetProfile
class SkillTestSet {

    @Autowired
    private lateinit var repository: SkillEntityRepository

    @Value("classpath:/test-set/skills.csv")
    private lateinit var resource: Resource

    fun reset() {
        val csvToBean = CsvToBeanBuilder<SkillRow>(InputStreamReader(resource.inputStream))
            .withType(SkillRow::class.java)
            .withSeparator(',')
            .withQuoteChar('"')
            .build()
        val skills = csvToBean.map {
            val skillEntity = SkillEntity(it.id)
            skillEntity.name = it.name
            skillEntity.slug = it.slug
            skillEntity
        }

        repository.deleteAll()
        repository.saveAll(skills)
    }
}
