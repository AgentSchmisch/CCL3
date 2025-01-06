package at.florianschmid.fridgeventory.data.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDate(date: LocalDateTime): String {
        return date.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(date: String): LocalDateTime {
        return LocalDateTime.parse(date, formatter)
    }
}