package ru.tbank.education.school.lesson2.ZooHomework

sealed class ZooEvent {
    object ZooOpened : ZooEvent()
    object ZooClosed : ZooEvent()
    data class AnimalAdded(val animalName: String) : ZooEvent()
    data class FeedingTime(val animalName: String) : ZooEvent()
}