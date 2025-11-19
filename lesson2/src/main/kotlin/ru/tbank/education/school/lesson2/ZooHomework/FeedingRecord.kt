package ru.tbank.education.school.lesson2.ZooHomework

data class FeedingRecord(
    val animalName: String,
    val foodType: String,
    val amount: Double,
    val keeperName: String
) {
    val formattedInfo: String
        get() = "$keeperName покормил $animalName - $foodType ($amount кг)"
}