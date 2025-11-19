package ru.tbank.education.school.lesson2.ZooHomework

class Predator(
    name: String,
    age: Int,
    weight: Double,
    furColor: String,
    private val huntingSkill: String
) : Mammal(name, age, weight, furColor) {

    override fun makeSound() {
        println("$name рычит!")
    }

    fun hunt() {
        val success = true
        if (success) {
            println("$name успешно поохотился!")
            updateHealthStatus("Сытый после охоты")
        }
    }

    override fun displayInfo() {
        super.displayInfo()
        println("Навык охоты: $huntingSkill")
    }
}