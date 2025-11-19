package ru.tbank.education.school.lesson2.ZooHomework

class Zoo(val name: String, val location: String) {
    private val enclosures: MutableList<Enclosure> = mutableListOf()
    private val feedingRecords: MutableList<FeedingRecord> = mutableListOf()

    fun addEnclosure(enclosure: Enclosure) {
        enclosures.add(enclosure)
        println("Добавлен вольер: ${enclosure.name}")
    }

    fun feedAnimal(animalName: String, foodType: String, amount: Double, keeperName: String) {
        val record = FeedingRecord(animalName, foodType, amount, keeperName)
        feedingRecords.add(record)
        println(record.formattedInfo)
    }

    fun displayZooInfo() {
        println("\n≽^•⩊•^≼ Зоопарк: $name ($location) ≽^•⩊•^≼")
        println("Количество вольеров: ${enclosures.size}")
        enclosures.forEach { enclosure ->
            println("Вольер ${enclosure.name}: ${enclosure.type}, животных: ${enclosure.currentOccupancy}")
        }
    }

    fun openZoo() {
        println("Зоопарк $name открыт!")
    }

    fun closeZoo() {
        println("Зоопарк $name закрыт!")
    }
}