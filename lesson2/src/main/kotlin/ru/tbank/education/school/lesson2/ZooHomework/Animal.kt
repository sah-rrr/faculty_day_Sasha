package ru.tbank.education.school.lesson2.ZooHomework

abstract class Animal(
    val name: String,
    val age: Int,
    val weight: Double
) {
    abstract val habitat: String
    protected var healthStatus: String = "Здоров"

    val isAdult: Boolean
        get() = age >= 1

    abstract fun makeSound()

    open fun displayInfo() {
        println("$name, возраст: $age лет, вес: ${weight}кг")
        println("Среда обитания: $habitat")
        println("Состояние здоровья: $healthStatus")
        println("Взрослое животное: $isAdult")
    }

    protected fun updateHealthStatus(status: String) {
        healthStatus = status
    }
}