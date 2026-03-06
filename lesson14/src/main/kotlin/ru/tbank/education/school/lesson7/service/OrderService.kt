package ru.tbank.education.school.lesson7.service

import ru.tbank.education.school.lesson7.dto.*
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
open class OrderService {

    private val orders = mutableMapOf<Long, Order>()
    private var idCounter = 1L

    open fun getAll(status: OrderStatus?): List<Order> {
        return if (status != null) {
            orders.values.filter { it.status == status }
        } else {
            orders.values.toList()
        }
    }

    open fun getById(id: Long): Order {
        return orders[id] ?: throw NoSuchElementException("Order with id=$id not found")
    }

    open fun create(request: CreateOrderRequest): Order {
        val order = toOrder(idCounter++, request, OrderStatus.NEW)
        orders[order.id] = order
        return order
    }

    open fun update(id: Long, request: CreateOrderRequest): Order {
        val existing = orders[id] ?: throw NoSuchElementException("Order with id=$id not found")
        val updated = toOrder(id, request, existing.status)
        orders[id] = updated
        return updated
    }

    open fun delete(id: Long) {
        if (!orders.containsKey(id)) throw NoSuchElementException("Order with id=$id not found")
        orders.remove(id)
    }

    private fun toOrder(id: Long, request: CreateOrderRequest, status: OrderStatus): Order {
        val items = request.items.map { item ->
            val lineTotal = item.price.multiply(BigDecimal.valueOf(item.quantity.toLong()))
            OrderItem(
                sku = item.sku,
                quantity = item.quantity,
                price = item.price,
                lineTotal = lineTotal
            )
        }
        val total = items.fold(BigDecimal.ZERO) { acc, item -> acc.add(item.lineTotal) }
        return Order(
            id = id,
            customerEmail = request.customerEmail,
            deliveryAddress = request.deliveryAddress,
            items = items,
            totalAmount = total,
            status = status
        )
    }
}
