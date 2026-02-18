package ru.tbank.education.school.lesson11

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Задание: Параллельное преобразование элементов списка с использованием async.
 *
 * Преобразуйте каждый элемент списка в отдельной корутине с помощью async.
 *
 * @param items список элементов для преобразования
 * @param transform функция преобразования
 * @return список преобразованных элементов в исходном порядке
 */
suspend fun <T, R> parallelTransform(
    items: List<T>,
    transform: suspend (T) -> R
): List<R> = coroutineScope {
    val deferreds = items.map { item ->
        async {
            transform(item)
        }
    }
    deferreds.awaitAll()
}

suspend fun <T, R> parallelTransformWithDispatcher(
    items: List<T>,
    dispatcher: CoroutineContext = Dispatchers.Default,
    transform: suspend (T) -> R
): List<R> = coroutineScope {

    items.map { item ->
        async(dispatcher) {
            transform(item)
        }
    }.awaitAll()
}