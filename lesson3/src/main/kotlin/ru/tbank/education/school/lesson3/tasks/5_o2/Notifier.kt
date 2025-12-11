import java.time.LocalTime
import java.time.format.DateTimeFormatter

interface ParentNotifier {
    val to: String
    val text: String
    fun sendMessage(): String
}

class Email(
    override val to: String,
    override val text: String,
    val subject: String
) : ParentNotifier {
    override fun sendMessage(): String {
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        return "Отправляю сообщение в ${this::class.simpleName} пользователю $to: $text в $time"
    }

    override fun toString(): String = "Email(subject='$subject')"
}

class Telegram(
    override val to: String,
    override val text: String,
    val chatId: Long
) : ParentNotifier {
    override fun sendMessage(): String {
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        return "Отправляю сообщение в ${this::class.simpleName} пользователю $to: $text в $time"
    }

    override fun toString(): String = "Telegram(chatId=$chatId)"
}

class SMS(
    override val to: String,
    override val text: String,
    val operator: String
) : ParentNotifier {
    override fun sendMessage(): String {
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        return "Отправляю сообщение в ${this::class.simpleName} пользователю $to: $text в $time"
    }

    override fun toString(): String = "SMS(operator='$operator')"
}

fun printNotification(n: ParentNotifier) {
    println("Класс: ${n::class.simpleName}")
    println("Уникальные параметры: $n")
    println(n.sendMessage())
    println()
}

fun main() {
    val email = Email("user@mail.com", "Здраствуйте! Прислала отчёт по первому кварталу.", subject = "Важное письмо")
    val tg = Telegram("@stasik", "привет, подскажи по дз пожалуйста", chatId = 111199)
    val sms = SMS("89154304020", "Код подтверждения: 1234", operator = "T-MOBILE")

    printNotification(email)
    printNotification(tg)
    printNotification(sms)
}