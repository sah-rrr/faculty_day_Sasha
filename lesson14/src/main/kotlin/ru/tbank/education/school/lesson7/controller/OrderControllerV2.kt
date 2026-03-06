package ru.tbank.education.school.lesson7.controller

import ru.tbank.education.school.lesson7.dto.CreateOrderRequest
import ru.tbank.education.school.lesson7.dto.Order
import ru.tbank.education.school.lesson7.dto.OrderStatus
import ru.tbank.education.school.lesson7.service.OrderService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
@Validated
open class OrderControllerV2(
    private val orderService: OrderService
) {

    @GetMapping
    fun getAll(
        @RequestParam(required = false) status: OrderStatus?
    ): List<Order> = orderService.getAll(status)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateOrderRequest
    ): Order = orderService.create(request)

    @GetMapping("/{id}")
    fun getById(
        @PathVariable @Min(1) id: Long
    ): Order = orderService.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable @Min(1) id: Long,
        @Valid @RequestBody request: CreateOrderRequest
    ): Order = orderService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable @Min(1) id: Long
    ) = orderService.delete(id)
}
