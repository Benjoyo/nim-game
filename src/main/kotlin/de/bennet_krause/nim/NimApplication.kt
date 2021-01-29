package de.bennet_krause.nim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class NimApplication

fun main(args: Array<String>) {
    runApplication<NimApplication>(*args)
}
