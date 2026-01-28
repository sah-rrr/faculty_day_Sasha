package ru.tbank.education.school.lesson10

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
import kotlin.collections.iterator

fun main() {
    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "2026-01-22 09:10 | ID:043 | STATUS:sent",
        "2026-01-22 09:18 | ID:043 | STATUS:delivered",
        "TS=22/01/2026-09:05; status=sent; #044",
        "[22.01.2026 09:40] delivered (id:044)",
        "2026-01-22 09:20 | ID:045 | STATUS:sent",
        "[22.01.2026 09:33] delivered (id:045)",
        "   ts=22/01/2026-09:50; STATUS=Sent; #046   ",
        " [22.01.2026 10:05]   DELIVERED   (ID:046) "
    )

    val normalized = normalizeAllLogs(logs)
    val goodLogs = normalized.first
    val badLogs = normalized.second

    printNormalized(goodLogs, badLogs)

    val groupedLogs = groupLogsByID(goodLogs)
    val calculationResults = calculateDeliveryTimes(groupedLogs)
    val completeResults = calculationResults.first
    val incompleteIds = calculationResults.second
    val timeErrorIds = calculationResults.third

    printDeliveryReport(completeResults, incompleteIds, timeErrorIds)

    printHourlyStats(goodLogs)
    printDuplicateCheck(groupedLogs)
}

data class LogEntry(val dt: LocalDateTime, val id: Int, val status: String)

data class DeliveryResult(val id: Int, val duration: Long, val sentTime: LocalDateTime, val deliveredTime: LocalDateTime)

fun normalize(line: String): LogEntry? {
    val trimmed = line.trim()

    val patternA = Regex("""(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}).*?ID:(\d+).*?STATUS:(\w+)""", RegexOption.IGNORE_CASE)
    val matchA = patternA.find(trimmed)
    if (matchA != null) {
        val (year, month, day, hour, minute, idStr, status) = matchA.destructured
        return createLogEntry(year, month, day, hour, minute, idStr, status)
    }

    val patternB = Regex("""TS=(\d{2})/(\d{2})/(\d{4})-(\d{2}):(\d{2}).*?status=(\w+).*?#(\d+)""", RegexOption.IGNORE_CASE)
    val matchB = patternB.find(trimmed)
    if (matchB != null) {
        val (day, month, year, hour, minute, status, idStr) = matchB.destructured
        return createLogEntry(year, month, day, hour, minute, idStr, status)
    }

    val patternC = Regex("""\[(\d{2})\.(\d{2})\.(\d{4}) (\d{2}):(\d{2})].*?(\w+).*?\(id:(\d+)\)""", RegexOption.IGNORE_CASE)
    val matchC = patternC.find(trimmed)
    if (matchC != null) {
        val (day, month, year, hour, minute, status, idStr) = matchC.destructured
        return createLogEntry(year, month, day, hour, minute, idStr, status)
    }

    return null
}

fun createLogEntry(year: String, month: String, day: String, hour: String, minute: String, idStr: String, status: String): LogEntry {
    val dateTime = LocalDateTime.of(
        year.toInt(),
        month.toInt(),
        day.toInt(),
        hour.toInt(),
        minute.toInt()
    )

    return LogEntry(dateTime, idStr.toInt(), status.lowercase())
}

fun normalizeAllLogs(logs: List<String>): Pair<List<LogEntry>, List<String>> {
    val goodLogs = mutableListOf<LogEntry>()
    val badLogs = mutableListOf<String>()

    for (log in logs) {
        val normalized = normalize(log)
        if (normalized != null) {
            goodLogs.add(normalized)
        } else {
            badLogs.add(log)
        }
    }

    return Pair(goodLogs, badLogs)
}

fun printNormalized(goodLogs: List<LogEntry>, badLogs: List<String>) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    println("Нормализованные логи:")
    for (log in goodLogs) {
        val formattedDt = log.dt.format(formatter)
        println("dt: \"$formattedDt\", id: ${log.id}, status: \"${log.status}\"")
    }

    if (badLogs.isNotEmpty()) {
        println("Битые строки:")
        for (badLog in badLogs) {
            println(badLog)
        }
    }
}

fun groupLogsByID(logs: List<LogEntry>): Map<Int, List<LogEntry>> {
    val groups = mutableMapOf<Int, MutableList<LogEntry>>()

    for (log in logs) {
        val list = groups.getOrPut(log.id) { mutableListOf() }
        list.add(log)
    }

    return groups
}

fun calculateDeliveryTimes(groupedLogs: Map<Int, List<LogEntry>>): Triple<List<DeliveryResult>, List<Int>, List<Int>> {
    val results = mutableListOf<DeliveryResult>()
    val incompleteIds = mutableListOf<Int>()
    val timeErrorIds = mutableListOf<Int>()

    for ((id, logsForId) in groupedLogs) {
        val sentLog = logsForId.find { it.status == "sent" }
        val deliveredLog = logsForId.find { it.status == "delivered" }

        if (sentLog == null || deliveredLog == null) {
            incompleteIds.add(id)
            continue
        }

        val sentTime = sentLog.dt
        val deliveredTime = deliveredLog.dt

        if (deliveredTime.isBefore(sentTime)) {
            timeErrorIds.add(id)
            continue
        }

        val duration = Duration.between(sentTime, deliveredTime).toMinutes()
        results.add(DeliveryResult(id, duration, sentTime, deliveredTime))
    }

    return Triple(results, incompleteIds, timeErrorIds)
}

fun printDeliveryReport(results: List<DeliveryResult>, incompleteIds: List<Int>, timeErrorIds: List<Int>) {
    val sortedResults = results.sortedByDescending { it.duration }
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    println("Отчет по доставке:")

    println("1. Все заказы (отсортированы по убыванию времени доставки):")
    if (sortedResults.isEmpty()) {
        println("   Нет данных")
    } else {
        for (result in sortedResults) {
            val sentFormatted = result.sentTime.format(formatter)
            val deliveredFormatted = result.deliveredTime.format(formatter)
            println("   ID: ${result.id}, Отправка: $sentFormatted, Доставка: $deliveredFormatted, Время: ${result.duration} мин")
        }
    }

    println("2. Самый долгий заказ:")
    if (sortedResults.isNotEmpty()) {
        val longest = sortedResults.first()
        val sentFormatted = longest.sentTime.format(formatter)
        val deliveredFormatted = longest.deliveredTime.format(formatter)
        println("   ID: ${longest.id}, Отправка: $sentFormatted, Доставка: $deliveredFormatted, Время: ${longest.duration} мин")
    } else {
        println("   Нет данных")
    }

    println("3. Нарушители (доставка дольше 20 минут):")
    val ruleBreakers = sortedResults.filter { it.duration > 20 }
    if (ruleBreakers.isEmpty()) {
        println("   Нарушителей нет")
    } else {
        for (ruleBreaker in ruleBreakers) {
            val sentFormatted = ruleBreaker.sentTime.format(formatter)
            val deliveredFormatted = ruleBreaker.deliveredTime.format(formatter)
            println("   ID: ${ruleBreaker.id}, Отправка: $sentFormatted, Доставка: $deliveredFormatted, Время: ${ruleBreaker.duration} мин")
        }
    }

    if (incompleteIds.isNotEmpty()) {
        println("4. Неполные заказы (не отправлен/доставлен):")
        println("   ID: ${incompleteIds.joinToString(", ")}")
    }

    if (timeErrorIds.isNotEmpty()) {
        println("5. Ошибки времени (доставка раньше отправки):")
        println("   ID: ${timeErrorIds.joinToString(", ")}")
    }
}

fun printHourlyStats(logs: List<LogEntry>) {
    println("'Час пик':")

    val hourCounts = mutableMapOf<Int, Int>()

    for (log in logs) {
        if (log.status == "delivered") {
            val hour = log.dt.hour
            hourCounts[hour] = hourCounts.getOrDefault(hour, 0) + 1
        }
    }

    if (hourCounts.isNotEmpty()) {
        val maxEntry = hourCounts.maxByOrNull { it.value }
        println("   Больше всего доставок в ${maxEntry?.key}:00 - ${maxEntry?.value} доставок")
    } else {
        println("   Нет данных о доставках")
    }
}

fun printDuplicateCheck(groupedLogs: Map<Int, List<LogEntry>>) {
    println("Проверка на дубли:")

    var hasDuplicates = false

    for ((id, logsForId) in groupedLogs) {
        val sentCount = logsForId.count { it.status == "sent" }
        val deliveredCount = logsForId.count { it.status == "delivered" }

        if (sentCount > 1 || deliveredCount > 1) {
            println("   ID $id: sent=$sentCount, delivered=$deliveredCount")
            hasDuplicates = true
        }
    }

    if (!hasDuplicates) {
        println("   Дублей не обнаружено")
    }
}