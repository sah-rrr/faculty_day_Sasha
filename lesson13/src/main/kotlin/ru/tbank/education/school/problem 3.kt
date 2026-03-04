package ru.tbank.education.school

import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import com.google.gson.Gson

// ===========================================
// Задача 3. JWT — авторизация
// ===========================================
// Цель: понять структуру JWT, собрать и декодировать токен, отправить запрос с Bearer-авторизацией.
// API: https://httpbin.org/bearer (возвращает 200 если есть Bearer, 401 если нет)
//
// TODO 1: Собрать JWT из трёх частей (header, payload, signature) в Base64URL
// TODO 2: Декодировать JWT обратно — вывести header и payload как JSON
// TODO 3: Отправить GET https://httpbin.org/bearer с заголовком Authorization: Bearer <token>
// TODO 4: Отправить тот же запрос БЕЗ токена — убедиться, что вернулся 401
// TODO 5: Подменить payload (role: student → admin), объяснить почему сервер отвергнет
//
// Подсказки:
//   Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) — кодирование
//   Base64.getUrlDecoder().decode(string)                        — декодирование
//   JWT = base64(header) + "." + base64(payload) + "." + base64(signature)

fun main() {
    // ===== TODO 1: Собрать JWT =====
    println("=== 1. Собрать JWT ===\n")

    val header = mapOf(
        "alg" to "HS256",
        "typ" to "JWT"
    )

    val payload = mapOf(
        "userId" to 123,
        "username" to "student_anna",
        "role" to "student",
        "exp" to (System.currentTimeMillis() / 1000 + 3600)
    )

    val headerJson = Gson().toJson(header)
    val payloadJson = Gson().toJson(payload)

    val headerBase64 = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(headerJson.toByteArray())
    val payloadBase64 = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(payloadJson.toByteArray())

    println("Header (JSON): $headerJson")
    println("Header (Base64): $headerBase64")
    println("\nPayload (JSON): $payloadJson")
    println("Payload (Base64): $payloadBase64")

    val signature = "simulated_signature_for_demo"
    val signatureBase64 = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(signature.toByteArray())

    val jwt = "$headerBase64.$payloadBase64.$signatureBase64"
    println("\nПолный JWT токен: $jwt")

    // ===== TODO 2: Декодировать JWT =====
    println("\n=== 2. Декодировать JWT ===\n")

    val parts = jwt.split(".")
    println("Количество частей: ${parts.size}")

    val decodedHeader = Base64.getUrlDecoder().decode(parts[0])
    val headerJsonDecoded = String(decodedHeader)
    println("Header после декодирования: $headerJsonDecoded")

    val decodedPayload = Base64.getUrlDecoder().decode(parts[1])
    val payloadJsonDecoded = String(decodedPayload)
    println("Payload после декодирования: $payloadJsonDecoded")

    // ===== TODO 3: Отправить запрос С токеном =====
    println("\n=== 3. Отправить запрос С токеном ===\n")

    val urlWithToken = URL("https://httpbin.org/bearer")
    val connectionWithToken = urlWithToken.openConnection() as HttpURLConnection
    connectionWithToken.requestMethod = "GET"
    connectionWithToken.setRequestProperty("Authorization", "Bearer $jwt")

    val codeWithToken = connectionWithToken.responseCode
    val responseWithToken = if (codeWithToken == 200) {
        connectionWithToken.inputStream.bufferedReader().readText()
    } else {
        connectionWithToken.errorStream?.bufferedReader()?.readText() ?: "Нет тела ответа"
    }

    println("Код ответа: $codeWithToken")
    println("Тело ответа: $responseWithToken")
    connectionWithToken.disconnect()

    // ===== TODO 4: Отправить запрос БЕЗ токена =====
    println("\n=== 4. Отправить запрос БЕЗ токена ===\n")

    val urlWithoutToken = URL("https://httpbin.org/bearer")
    val connectionWithoutToken = urlWithoutToken.openConnection() as HttpURLConnection
    connectionWithoutToken.requestMethod = "GET"

    val codeWithoutToken = connectionWithoutToken.responseCode
    val responseWithoutToken = if (codeWithoutToken == 200) {
        connectionWithoutToken.inputStream.bufferedReader().readText()
    } else {
        connectionWithoutToken.errorStream?.bufferedReader()?.readText() ?: "Нет тела ответа"
    }

    println("Код ответа: $codeWithoutToken")
    println("Тело ответа: $responseWithoutToken")
    connectionWithoutToken.disconnect()

    // ===== TODO 5: Подмена payload =====
    println("\n=== 5. Подмена payload ===\n")

    val fakePayload = mapOf(
        "userId" to 123,
        "username" to "student_anna",
        "role" to "admin",
        "exp" to (System.currentTimeMillis() / 1000 + 3600)
    )

    val fakePayloadJson = Gson().toJson(fakePayload)
    val fakePayloadBase64 = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(fakePayloadJson.toByteArray())

    val fakeJwt = "$headerBase64.$fakePayloadBase64.$signatureBase64"

    println("Оригинальный payload: $payloadJson")
    println("Поддельный payload: $fakePayloadJson")
    println("\nПоддельный JWT: $fakeJwt")

    val urlWithFakeToken = URL("https://httpbin.org/bearer")
    val connectionWithFakeToken = urlWithFakeToken.openConnection() as HttpURLConnection
    connectionWithFakeToken.requestMethod = "GET"
    connectionWithFakeToken.setRequestProperty("Authorization", "Bearer $fakeJwt")

    val codeWithFakeToken = connectionWithFakeToken.responseCode
    val responseWithFakeToken = if (codeWithFakeToken == 200) {
        connectionWithFakeToken.inputStream.bufferedReader().readText()
    } else {
        connectionWithFakeToken.errorStream?.bufferedReader()?.readText() ?: "Нет тела ответа"
    }

    println("\nКод ответа с поддельным токеном: $codeWithFakeToken")
    println("Тело ответа: $responseWithFakeToken")
    connectionWithFakeToken.disconnect()
}