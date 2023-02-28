package nl.runnable.gigmatch.domain

/**
 * Defines operations for predicates/preconditions used to enforce domain invariants.
 */
interface Specification<T> {

    fun isSatisfiedBy(subject: T): Boolean
}
