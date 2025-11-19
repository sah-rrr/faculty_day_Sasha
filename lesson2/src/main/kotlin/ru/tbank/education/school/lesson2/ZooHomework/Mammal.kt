package ru.tbank.education.school.lesson2.ZooHomework

open class Mammal(
    name: String,
    age: Int,
    weight: Double,
    private val furColor: String
) : Animal(name, age, weight) {

    override val habitat: String = "Наземная"
    private var numberOfOffspring: Int = 0

    override fun makeSound() {
        println("$name разговаривает")
    }

    fun giveBirth() {
        val offspring = 2
        numberOfOffspring += offspring
        updateHealthStatus("После родов")
        println("$name родила $offspring детенышей")
    }

    override fun displayInfo() {
        super.displayInfo()
        println("Цвет шерсти: $furColor")
        println("Всего родилось детенышей: $numberOfOffspring")
    }
}