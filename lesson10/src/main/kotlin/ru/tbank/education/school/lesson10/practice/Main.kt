package ru.tbank.education.school.lesson10.practice

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Period
import java.time.format.DateTimeFormatter

fun main() {
    task1()
    println()
    task2()
    println()
    task3()
    println()
    task4()
    println()
    task5()
    println()
    task6()
    println()
    task7()
    println()
    task8()
}

/*
1) Строки + регулярные выражения
["Name: Ivan, score=17", ...]
Извлечь имя и score, собрать пары, вывести победителя.
*/
fun task1() {
    val lines = listOf(
        "Name: Ivan, score=17",
        "Name: Olga, score=23",
        "Name: Max, score=5"
    )

    val re = Regex("""^Name:\s*([A-Za-z]+)\s*,\s*score=(\d+)\s*$""")

    val pairs: List<Pair<String, Int>> = lines.mapNotNull { s ->
        val m = re.find(s) ?: return@mapNotNull null
        val name = m.groupValues[1]
        val score = m.groupValues[2].toInt()
        name to score
    }

    println("Task 1 pairs: $pairs")

    val best = pairs.maxByOrNull { it.second }
    if (best != null) {
        println("Task 1 best: ${best.first} (${best.second})")
    } else {
        println("Task 1: no valid lines")
    }
}

/*
2) Даты + коллекции
["2026-01-22", ...]
Преобразовать в даты, отсортировать, посчитать сколько в январе 2026.
*/
fun task2() {
    val dateStrings = listOf(
        "2026-01-22",
        "2026-02-01",
        "2025-12-31",
        "2026-01-05"
    )

    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    val dates = dateStrings.map { LocalDate.parse(it, fmt) }.sorted()

    println("Task 2 sorted dates: ${dates.joinToString { it.format(fmt) }}")

    val countJan2026 = dates.count { it.year == 2026 && it.month == Month.JANUARY }
    println("Task 2 count in Jan 2026: $countJan2026")
}

/*
3) Коллекции + строки
"apple orange apple banana orange apple"
Частоты слов, вывести слова с частотой > 1 по алфавиту.
*/
fun task3() {
    val text = "apple orange apple banana orange apple"

    val words = text.trim().split(" ").filter { it.isNotEmpty() }

    val freq = mutableMapOf<String, Int>()
    for (w in words) {
        freq[w] = (freq[w] ?: 0) + 1
    }

    println("Task 3 freq: $freq")

    val repeated = freq
        .filter { (_, c) -> c > 1 }
        .keys
        .sorted()

    println("Task 3 repeated words: ${repeated.joinToString(", ")}")
}

fun task4() {
    val lines = listOf(
        "A-123",
        "B-7",
        "AA-12",
        "C-001",
        "D-99x"
    )

    lines.filter { it.matches(Regex("^\\s*[A-Z]-\\d{1,3}\\s*$")) }

    println("Task 4 filtered lines: ${lines.joinToString(", ")}")
}

fun task5() {
    val lines = listOf(
        "  Hello   world  ",
        "A   B    C",
        "   one"
    )

    lines.map { it.trim().replace("\\s+".toRegex(), " ") }

    println("Task 5 formatted lines: ${lines.joinToString(", ")}")
}

fun task6() {
    val dates = listOf(
        Pair("2026-01-01","2026-01-10"),
        Pair("2025-12-31","2026-01-01"),
        Pair("2026-02-01","2026-01-22")
    ).map {
        LocalDate.parse(it.first, DateTimeFormatter.ISO_LOCAL_DATE) to
                LocalDate.parse(it.second, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    val diff = dates.map { (start, end) -> Period.between(start, end).days }

    println("Task 6 differences: ${diff.joinToString(", ")}")
}

fun task7() {
    val lines = listOf(
        "math:Ivan",
        "bio:Olga",
        "math:Max",
        "bio:Ivan", "cs:Olga"
    ).map { it.split(':') }

    lines.associate { it[0] to it[1] }

    println("Task 7 map: $lines")
}

fun task8() {
    val lines = listOf(
        "Start at 2026/01/22 09:14",
        "No time here",
        "End: 22-01-2026 18:05"
    )

    val formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    val formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    print("Task 8 formatted: ")
    lines.forEach { str ->
        val match1 = Regex("""\d{4}/\d{2}/\d{2} \d{2}:\d{2}""").find(str)
        if (match1 != null) {
            val dateTime = LocalDateTime.parse(match1.value, formatter1)
            print("${dateTime.format(outputFormatter)}   ")
            return@forEach
        }

        val match2 = Regex("""\d{2}-\d{2}-\d{4} \d{2}:\d{2}""").find(str)
        if (match2 != null) {
            val dateTime = LocalDateTime.parse(match2.value, formatter2)
            print("${dateTime.format(outputFormatter)}  ")
        }
    }
}