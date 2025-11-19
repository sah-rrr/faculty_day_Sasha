package ru.tbank.education.school.lesson2.ZooHomework

open class Bird(
    name: String,
    age: Int,
    weight: Double,
    private val wingspan: Double,
    val canFly: Boolean
) : Animal(name, age, weight) {

    override val habitat: String = "Воздушная"
    private var flightDistance: Double = 0.0

    override fun makeSound() {
        println("$name поёт")
    }

    fun fly(distance: Double) {
        if (canFly) {
            flightDistance += distance
            println("$name пролетел $distance км")
        } else {
            println("$name не может летать")
        }
    }

    override fun displayInfo() {
        super.displayInfo()
        println("Размах крыльев: ${wingspan}м")
        println("Умеет летать: $canFly")
        println("Общая дистанция полетов: ${flightDistance}км")
    }
}