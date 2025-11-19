package ru.tbank.education.school.lesson2.ZooHomework

class Waterfowl(
    name: String,
    age: Int,
    weight: Double,
    wingspan: Double,
    canFly: Boolean,
    private val swimmingSpeed: Double
) : Bird(name, age, weight, wingspan, canFly) {

    override fun makeSound() {
        println("$name крякает!")
    }

    fun swim(distance: Double) {
        println("$name проплыл $distance метров")
    }

    override fun displayInfo() {
        super.displayInfo()
        println("Скорость плавания: ${swimmingSpeed} м/с")
    }
}