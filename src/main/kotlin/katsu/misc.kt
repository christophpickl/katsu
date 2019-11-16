package katsu

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun datePatternParse(text: String): LocalDateTime {
    val date = LocalDate.parse(text, datePattern)
    return LocalDateTime.parse("2000-01-01T00:00:00.000")
        .withYear(date.year)
        .withMonth(date.month.value)
        .withDayOfMonth(date.dayOfMonth)

}

fun LocalDateTime.formatKatsuDate(): String = format(datePattern)
