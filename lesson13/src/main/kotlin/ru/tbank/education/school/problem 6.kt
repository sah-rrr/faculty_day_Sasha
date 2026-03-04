package ru.tbank.education.school

import java.net.HttpURLConnection
import java.net.URL

// ===========================================
// Задача 6. Клиент для сервера заметок
// ===========================================
// Цель: написать клиент, который тестирует все эндпоинты сервера.
// Перед запуском: запустить Task6_Server.kt
//
// TODO 1: Реализовать request() — универсальную функцию отправки запросов
// TODO 2: В main() выполнить 8 шагов (ниже), вывести код и тело каждого ответа

val BASE = "http://localhost:8080/api/notes"

/** Отправить HTTP-запрос.
 *  @param url    — полный URL
 *  @param method — HTTP-метод
 *  @param body   — JSON-тело (null для GET/DELETE)
 *  @return Pair(statusCode, responseBody)
 */

fun request(url: String, method: String, body: String? = null): Pair<Int, String> {
    val connection = URL(url).openConnection() as HttpURLConnection

    connection.requestMethod = method

    if (body != null) {
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val outputStream = connection.outputStream
        outputStream.write(body.toByteArray())
        outputStream.flush()
        outputStream.close()
    }

    val responseCode = connection.responseCode

    val responseBody = if (responseCode in 200..299) {
        val inputStream = connection.inputStream
        val text = inputStream.bufferedReader().readText()
        inputStream.close()
        text
    } else {
        val errorStream = connection.errorStream
        if (errorStream != null) {
            val text = errorStream.bufferedReader().readText()
            errorStream.close()
            text
        } else {
            "Нет тела ответа"
        }
    }

    connection.disconnect()

    return Pair(responseCode, responseBody)
}

fun main() {
    println("=== 1. GET /api/notes — все заметки ===")
    val (code1, body1) = request(BASE, "GET")
    println("Код: $code1")
    println("Тело: $body1")
    println()

    println("=== 2. POST /api/notes — создать заметку ===")
    val newNote = "{\"title\":\"Домашка\",\"content\":\"Сделать задание по сетям\",\"tag\":\"учёба\"}"
    val (code2, body2) = request(BASE, "POST", newNote)
    println("Код: $code2")
    println("Тело: $body2")
    println()

    println("=== 3. GET /api/notes/1 — одна заметка ===")
    val (code3, body3) = request("$BASE/1", "GET")
    println("Код: $code3")
    println("Тело: $body3")
    println()

    println("=== 4. PUT /api/notes/1 — обновить заметку ===")
    val updatedNote = "{\"title\":\"Покупки (обновлено)\",\"content\":\"Молоко, хлеб, яйца, сыр\",\"tag\":\"личное\"}"
    val (code4, body4) = request("$BASE/1", "PUT", updatedNote)
    println("Код: $code4")
    println("Тело: $body4")
    println()

    println("=== 5. GET /api/notes?tag=учёба — фильтр по тегу ===")
    val (code5, body5) = request("$BASE?tag=учёба", "GET")
    println("Код: $code5")
    println("Тело: $body5")
    println()

    println("=== 6. DELETE /api/notes/1 — удалить заметку ===")
    val (code6, body6) = request("$BASE/1", "DELETE")
    println("Код: $code6")
    println("Тело: $body6")
    println()

    println("=== 7. GET /api/notes/999 — несуществующая заметка ===")
    val (code7, body7) = request("$BASE/999", "GET")
    println("Код: $code7")
    println("Тело: $body7")
    println()

    println("=== 8. GET /api/notes — финальное состояние ===")
    val (code8, body8) = request(BASE, "GET")
    println("Код: $code8")
    println("Тело: $body8")
}