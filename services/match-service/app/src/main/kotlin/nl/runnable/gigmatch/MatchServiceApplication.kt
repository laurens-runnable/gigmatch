package nl.runnable.gigmatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MatchServiceApplication

fun main(args: Array<String>) {
    runApplication<MatchServiceApplication>(*args)
}
