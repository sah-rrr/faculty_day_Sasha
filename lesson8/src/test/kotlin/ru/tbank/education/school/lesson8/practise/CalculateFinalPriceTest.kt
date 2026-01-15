package ru.tbank.education.school.lesson8.practise

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.tbank.education.school.lesson8.lection.calculator.CalculatorTest
import kotlin.test.assertEquals

/**
 *
 * Сценарии для тестирования:
 *
 * 1. Позитивные сценарии (happy path):
 *    - Обычный случай: basePrice = 1000, discount = 10%, tax = 20% → проверить корректность формулы.
 *    - Без скидки: discountPercent = 0 → итог = basePrice + налог.
 *    - Без налога: taxPercent = 0 → итог = basePrice минус скидка.
 *    - Без скидки и без налога: итог = basePrice.
 *
 * 2. Негативные сценарии (исключения):
 *    - Отрицательная цена: basePrice < 0 → IllegalArgumentException.
 *    - Скидка вне диапазона: discountPercent < 0 или > 100 → IllegalArgumentException.
 *    - Налог вне диапазона: taxPercent < 0 или > 30 → IllegalArgumentException.
 */

class CalculateFinalPriceTest {

    @Test
    fun `usual case with discount and tax should calculate correctly`() {
        val result = calculateFinalPrice(basePrice = 1000.0, discountPercent = 10, taxPercent = 20)
        assertEquals(1080.0, result, 0.001)
    }

    @Test
    fun `no discount only tax should calculate correctly`() {
        val result = calculateFinalPrice(basePrice = 500.0, discountPercent = 0, taxPercent = 15)
        assertEquals(575.0, result, 0.001)
    }

    @Test
    fun `no tax only discount should calculate correctly`() {
        val result = calculateFinalPrice(basePrice = 800.0, discountPercent = 25, taxPercent = 0)
        assertEquals(600.0, result, 0.001)
    }

    @Test
    fun `no discount and no tax should return base price`() {
        val result = calculateFinalPrice(basePrice = 300.0, discountPercent = 0, taxPercent = 0)
        assertEquals(300.0, result, 0.001)
    }

    @Test
    fun `zero base price should return zero`() {
        val result = calculateFinalPrice(basePrice = 0.0, discountPercent = 10, taxPercent = 20)
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun `maximum discount should make price zero`() {
        val result = calculateFinalPrice(basePrice = 100.0, discountPercent = 100, taxPercent = 10)
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun `maximum tax should calculate correctly`() {
        val result = calculateFinalPrice(basePrice = 200.0, discountPercent = 5, taxPercent = 30)
        assertEquals(247.0, result, 0.001)
    }

    @Test
    fun `small price with discount should calculate correctly`() {
        val result = calculateFinalPrice(basePrice = 0.01, discountPercent = 50, taxPercent = 10)
        assertEquals(0.0055, result, 0.000001)
    }

    @Test
    fun `price with 50 percent discount and 20 percent tax`() {
        val result = calculateFinalPrice(basePrice = 200.0, discountPercent = 50, taxPercent = 20)
        assertEquals(120.0, result, 0.001)
    }

    @Test
    fun `price with 75 percent discount and no tax`() {
        val result = calculateFinalPrice(basePrice = 400.0, discountPercent = 75, taxPercent = 0)
        assertEquals(100.0, result, 0.001)
    }

    @Test
    fun `price with no discount and maximum tax`() {
        val result = calculateFinalPrice(basePrice = 100.0, discountPercent = 0, taxPercent = 30)
        assertEquals(130.0, result, 0.001)
    }

    @Test
    fun `price becomes zero after discount then tax`() {
        val result = calculateFinalPrice(basePrice = 10.0, discountPercent = 100, taxPercent = 20)
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun `large price with small discount and tax`() {
        val result = calculateFinalPrice(basePrice = 10000.0, discountPercent = 5, taxPercent = 18)
        assertEquals(11210.0, result, 0.001)
    }
}