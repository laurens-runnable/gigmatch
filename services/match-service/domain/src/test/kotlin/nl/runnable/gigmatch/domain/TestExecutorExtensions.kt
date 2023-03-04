package nl.runnable.gigmatch.domain

import org.axonframework.test.aggregate.ResultValidator
import org.axonframework.test.aggregate.TestExecutor

fun <T> TestExecutor<T>.whenever(obj: Any): ResultValidator<T> {
    return this.`when`(obj)
}
