package nl.runnable.gigmatch.framework.axon

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
class RepositoryHelper {

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    private lateinit var entityManager: EntityManager

    fun clearRepositories() {
        transactionTemplate.execute {
            with(entityManager) {
                createQuery("DELETE FROM AssociationValueEntry ").executeUpdate()
                createQuery("DELETE FROM DeadLetterEntry").executeUpdate()
                createQuery("DELETE FROM DomainEventEntry").executeUpdate()
                createQuery("DELETE FROM SagaEntry").executeUpdate()
                createQuery("DELETE FROM SnapshotEventEntry ").executeUpdate()
                createQuery("DELETE FROM TokenEntry").executeUpdate()
            }
        }
    }
}
