package ru.tbank.education.school.lesson2

import ru.tbank.education.school.lesson2.ZooHomework.Bird
import ru.tbank.education.school.lesson2.ZooHomework.Enclosure
import ru.tbank.education.school.lesson2.ZooHomework.Mammal
import ru.tbank.education.school.lesson2.ZooHomework.Predator
import ru.tbank.education.school.lesson2.ZooHomework.Waterfowl
import ru.tbank.education.school.lesson2.ZooHomework.Zoo

fun main() {
    // создаем зоопарк
    val zoo = Zoo("Зоопарк", "Москва")
    zoo.openZoo()

    // создаем вольеры
    val predatorEnclosure = Enclosure("Для хищников", "Большой вольер", 3)
    val birdEnclosure = Enclosure("Для птиц", "Вольер с сеткой", 4)
    val mammalEnclosure = Enclosure("Для млекопитающих", "Лесной вольер", 3)

    zoo.addEnclosure(predatorEnclosure)
    zoo.addEnclosure(birdEnclosure)
    zoo.addEnclosure(mammalEnclosure)

    // создаем животных
    val lion = Predator("Лев Симба", 5, 190.0, "Золотистый", "Высокий")
    val tiger = Predator("Тигр Амур", 4, 220.0, "Оранжевый с черным", "Очень высокий")

    val eagle = Bird("Орел Глаз", 3, 6.5, 2.1, true)
    val duck = Waterfowl("Утка Кряква", 2, 1.2, 0.8, true, 1.5)
    val penguin = Waterfowl("Пингвин Пороро", 4, 25.0, 0.6, false, 2.0)

    val bear = Mammal("Медведь Копатыч", 8, 300.0, "Рыжий")
    val wolf = Mammal("Волк Одинокий", 3, 60.0, "Серый")

    // размещаем животных в вольерах
    predatorEnclosure.addAnimal(lion)
    predatorEnclosure.addAnimal(tiger)

    birdEnclosure.addAnimal(eagle)
    birdEnclosure.addAnimal(duck)
    birdEnclosure.addAnimal(penguin)

    mammalEnclosure.addAnimal(bear)
    mammalEnclosure.addAnimal(wolf)

    // взаимодействие с животными
    println("\n/ᐠ˵- ⩊ -˵マ Возможности животных /ᐠ˵- ⩊ -˵マ")

    // звуки животных
    lion.makeSound()
    eagle.makeSound()
    duck.makeSound()

    // действия животных
    lion.hunt()
    eagle.fly(15.0)
    duck.swim(50.0)

    // размножение
    bear.giveBirth()

    // кормление
    println("\nฅ^•ﻌ•^ฅ Кормление животных ฅ^•ﻌ•^ฅ")
    zoo.feedAnimal("Лев Симба", "Мясо", 5.0, "Иван")
    zoo.feedAnimal("Утка Кряка", "Зерно", 0.3, "Мария")

    // информация о зоопарке
    zoo.displayZooInfo()

    // информация о вольерах
    predatorEnclosure.displayAnimals()
    birdEnclosure.displayAnimals()

    // проверка кастомных геттеров
    println("\n/ᐠ - ˕ -マ Проверка дополнительной информации /ᐠ - ˕ -マ")
    println("Лев Симба взрослый: ${lion.isAdult}")
    println("Вольер для хищников заполнен: ${predatorEnclosure.isFull}")

    zoo.closeZoo()
}