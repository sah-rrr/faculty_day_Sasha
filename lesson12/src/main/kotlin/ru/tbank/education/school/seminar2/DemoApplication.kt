package ru.tbank.education.school.seminar2

import ru.tbank.education.school.seminar2.config.BookProperties
import ru.tbank.education.school.seminar2.service.BookService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(BookProperties::class)
class DemoApplication {

    private val log = LoggerFactory.getLogger(DemoApplication::class.java)

    @Bean
    fun demo(service: BookService): CommandLineRunner = CommandLineRunner {
        log.info("  Демо CRUD для Book")

        val b1 = service.create("The Hobbit", "Tolkien", "Fiction")
        val b2 = service.create("Clean Code", "Robert Martin", "Tech")
        val b3 = service.create("1984", "Orwell", "Fiction")
        log.info("Создано 3 книги")

        log.info("Все книги: ${service.getAll()}")
        log.info("Только Fiction: ${service.getAll(applyFilter = true)}")

        log.info("Книга #${b1.id}: ${service.getById(b1.id)}")

        val updated = service.update(b2.id, "Clean Code (2nd Ed)", "Robert Martin", "Tech")
        log.info("Обновлено: $updated")

        service.delete(b3.id)
        log.info("После удаления: ${service.getAll()}")

        try {
            service.create("Mystery", "Anonymous", "Fiction")
        } catch (e: Exception) {
            log.warn("Ожидаемая ошибка: ${e.message}")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
