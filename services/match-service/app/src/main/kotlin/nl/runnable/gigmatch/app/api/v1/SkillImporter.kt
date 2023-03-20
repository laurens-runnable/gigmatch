package nl.runnable.gigmatch.app.api.v1

import com.opencsv.bean.CsvToBeanBuilder
import nl.runnable.gigmatch.framework.vacancy.SkillEntity
import nl.runnable.gigmatch.framework.vacancy.SkillEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Reader

@Component
class SkillImporter {

    @Autowired
    private lateinit var repository: SkillEntityRepository

    fun importFromCsv(csv: Reader) {
        val csvToBean = CsvToBeanBuilder<SkillRow>(csv)
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
