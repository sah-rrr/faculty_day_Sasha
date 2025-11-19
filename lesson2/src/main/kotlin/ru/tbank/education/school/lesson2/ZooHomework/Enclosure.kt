package ru.tbank.education.school.lesson2.ZooHomework

class Enclosure(
    val name: String,
    val type: String,
    private val capacity: Int
) {
    private val animals: MutableList<Animal> = mutableListOf()
    val isFull: Boolean
        get() = animals.size >= capacity
    val currentOccupancy: Int
        get() = animals.size

    fun addAnimal(animal: Animal): Boolean {
        if (!isFull) {
            animals.add(animal)
            println("${animal.name} добавлен в вольер $name")
            return true
        } else {
            println("Вольер $name переполнен! Нельзя добавить ${animal.name}")
            return false
        }
    }

    fun displayAnimals() {
        println("\nЖивотные в вольере $name ($type):")
        if (animals.isEmpty()) {
            println("Вольер пуст")
        } else {
            animals.forEach { it.displayInfo() }
        }
    }
}