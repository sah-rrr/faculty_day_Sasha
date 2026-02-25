package ru.tbank.education.school.seminar2.service

import ru.tbank.education.school.seminar2.config.BookProperties
import ru.tbank.education.school.seminar2.model.Book
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BookService(private val props: BookProperties) {

    private val log = LoggerFactory.getLogger(BookService::class.java)
    private val books: MutableList<Book> = mutableListOf()
    private var nextId = 1L

    fun create(title: String, author: String, category: String): Book {
        if (books.size >= props.maxSize) {
            log.warn("Cannot add book: limit of ${props.maxSize} reached")
            error("Book limit reached: max ${props.maxSize}")
        }
        if (author in props.forbiddenAuthors) {
            log.warn("Author '$author' is forbidden")
            error("Author '$author' is not allowed")
        }
        val book = Book(id = nextId++, title = title, author = author, category = category)
        books.add(book)
        log.info("Created book: $book")
        return book
    }

    fun getAll(applyFilter: Boolean = false): List<Book> {
        return if (applyFilter && props.filterCategory.isNotBlank()) {
            log.info("Filtering books by category: ${props.filterCategory}")
            books.filter { it.category == props.filterCategory }
        } else {
            books.toList()
        }
    }

    fun getById(id: Long): Book {
        return books.find { it.id == id }
            ?: error("Book with id=$id not found")
    }

    fun update(id: Long, title: String, author: String, category: String): Book {
        val index = books.indexOfFirst { it.id == id }
        if (index == -1) error("Book with id=$id not found")
        if (author in props.forbiddenAuthors) error("Author '$author' is not allowed")
        val updated = Book(id, title, author, category)
        books[index] = updated
        log.info("Updated book: $updated")
        return updated
    }

    fun delete(id: Long) {
        val removed = books.removeIf { it.id == id }
        if (!removed) error("Book with id=$id not found")
        log.info("Deleted book with id=$id")
    }
}
