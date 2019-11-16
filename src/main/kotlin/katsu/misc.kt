package katsu

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val datePatternAsString = "dd-MM-yyyy"
val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern(datePatternAsString)

fun datePatternParse(text: String): LocalDateTime {
    val date = LocalDate.parse(text, datePattern)
    return LocalDateTime.parse("2000-01-01T00:00:00.000")
        .withYear(date.year)
        .withMonth(date.month.value)
        .withDayOfMonth(date.dayOfMonth)

}

fun LocalDateTime.formatKatsuDate(): String = format(datePattern)

fun LocalDateTime.withZeroTime(): LocalDateTime = withHour(0).withMinute(0).withSecond(0).withNano(0)
