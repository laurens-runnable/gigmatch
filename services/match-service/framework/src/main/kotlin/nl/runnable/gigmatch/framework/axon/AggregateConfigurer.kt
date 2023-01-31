package nl.runnable.gigmatch.framework.axon

import nl.runnable.gigmatch.domain.vacancy.Vacancy
import org.axonframework.config.Configurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
internal class AggregateConfigurer {

    @Autowired
    private lateinit var configurer: Configurer

    @PostConstruct
    fun registerAggregates() {
        configurer.configureAggregate(Vacancy::class.java)
    }

}
