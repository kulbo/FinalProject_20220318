package kr.co.smartsoft.finalproject_20220318.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateDeseralizer : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        val dateStr = json?.asString
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateStr)

        return date!!
    }

}