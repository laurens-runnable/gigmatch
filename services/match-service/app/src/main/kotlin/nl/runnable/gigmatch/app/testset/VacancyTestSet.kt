package nl.runnable.gigmatch.app.testset

import com.opencsv.bean.CsvToBeanBuilder
import nl.runnable.gigmatch.application.vacancy.VacancyUseCase
import nl.runnable.gigmatch.commands.toDomainCounterpart
import nl.runnable.gigmatch.domain.vacancy.Job
import nl.runnable.gigmatch.domain.vacancy.Rate
import nl.runnable.gigmatch.domain.vacancy.SkillId
import nl.runnable.gigmatch.domain.vacancy.Term
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
import java.io.InputStreamReader
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

        val csvToBean = CsvToBeanBuilder<VacancyRow>(InputStreamReader(resource.inputStream))
            .withType(VacancyRow::class.java)
            .withSeparator(',')
            .withQuoteChar('"')
            .build()
        csvToBean.map {
            val start = LocalDate.now().plusMonths(it.startMonthsFromNow).withDayOfMonth(1)
            val end = start.plusMonths(it.durationMonths)
            val deadline = start.minusWeeks(it.deadlineWeeksBefore)
            VacancyUseCase.CreateVacancyParams(
                VacancyId(it.id),
                Job(it.jobTitle, setOf(SkillId(it.skillId))),
                Term(start, end),
                Rate(it.rateAmount, it.rateType.toDomainCounterpart()),
                deadline,
            )
        }.forEach {
            vacancyUseCase.createVacancy(it)
        }
    }
}
