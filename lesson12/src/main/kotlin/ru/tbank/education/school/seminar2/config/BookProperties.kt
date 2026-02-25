package ru.tbank.education.school.seminar2.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "book")
data class BookProperties(
    val maxSize: Int = 10,
    val forbiddenAuthors: List<String> = emptyList(),
    val filterCategory: String = ""
)
