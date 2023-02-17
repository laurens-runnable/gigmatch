package nl.runnable.gigmatch.app.testset

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import nl.runnable.gigmatch.application.vacancy.VacancyUseCase
import nl.runnable.gigmatch.domain.vacancy.VacancyId
import nl.runnable.gigmatch.events.VacanciesReset
import nl.runnable.gigmatch.framework.axon.RepositoryHelper
import nl.runnable.gigmatch.framework.messaging.MATCH_EVENTS
import nl.runnable.gigmatch.framework.messaging.TypedMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
@TestSetProfile
class VacancyTestSet {

    @Autowired
    private lateinit var repositoryHelper: RepositoryHelper

    @Autowired
    private lateinit var streamBridge: StreamBridge

    @Autowired
    private lateinit var vacancyUseCase: VacancyUseCase

    @Value("classpath:/test-set/vacancies.csv")
    private lateinit var resource: Resource

    fun reset() {
        repositoryHelper.clearRepositories()

        streamBridge.send(MATCH_EVENTS, TypedMessage(VacanciesReset()))

        val rows = csvReader().readAllWithHeader(resource.inputStream)
        rows.map {
            val id = VacancyId(UUID.fromString(it["id"]))
            val name = it["name"]!!
            val monthsFromNow = Integer.parseInt(it["monthsFromNow"])
            val start = LocalDate.now().plusMonths(monthsFromNow.toLong()).withDayOfMonth(1)
            VacancyUseCase.CreateVacancyParams(id, name, start)
        }.forEach {
            vacancyUseCase.createVacancy(it)
        }
    }

}
