package ru.tbank.education.school.lesson1

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun calculate(a: Double, operation: OperationType? = OperationType.ADD, b: Double = 0.0): Double? {
    return when (operation) {
        OperationType.ADD -> a+b
        OperationType.SUBTRACT -> a-b
        OperationType.MULTIPLY -> a*b
        OperationType.DIVIDE -> if (b == 0.0) null else a/b
        OperationType.SIN -> sin(a)
        OperationType.COS -> cos(a)
        OperationType.SQRT -> sqrt(a)
        else -> a+b
    }
}

/**
 * Функция вычисления выражения, представленного строкой
 * @return результат вычисления строки или null, если вычисление невозможно
 * @sample "5 * 2".calculate()
 */
@Suppress("ReturnCount")
fun String.calculate(): Double? {
    val array = this.split(" ")
    val a = array[0].toDouble()
    if (array.size == 2) {
        val b = array[1].toDouble()
        return a + b
    } else {
        val b = array[2].toDouble()
        val op = array[1]
        return when (op) {
            "-" -> a-b
            "*" -> a*b
            "/" -> if (b == 0.0) null else a/b
            else -> a+b
        }
    }
}

fun main() {

}
