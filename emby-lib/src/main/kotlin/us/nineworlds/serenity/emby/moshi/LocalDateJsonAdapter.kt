package us.nineworlds.serenity.emby.moshi

import android.annotation.SuppressLint
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
class LocalDateJsonAdapter : JsonAdapter<LocalDateTime>() {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.")

    override fun fromJson(reader: JsonReader): LocalDateTime? {
        val dateTimeString = reader.nextString()?.replaceAfter(".", "") ?: return null
        return LocalDateTime.parse(dateTimeString, dateFormatter)
    }

    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value?.toString())
    }
}
