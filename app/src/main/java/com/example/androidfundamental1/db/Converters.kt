package com.example.androidfundamental1.db

import androidx.room.TypeConverter
import com.example.androidfundamental1.models.DetailEvent
import com.example.androidfundamental1.models.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Mengonversi DetailEvent menjadi String (JSON)
    @TypeConverter
    fun fromDetailEvent(detailEvent: DetailEvent): String {
        return gson.toJson(detailEvent)
    }

    // Mengonversi String (JSON) menjadi DetailEvent
    @TypeConverter
    fun toDetailEvent(json: String): DetailEvent {
        return gson.fromJson(json, DetailEvent::class.java)
    }

    // Jika ingin mengonversi List<Event> menjadi String
    @TypeConverter
    fun fromEventList(eventList: List<Event>): String {
        return gson.toJson(eventList)
    }

    // Mengonversi String (JSON) menjadi List<Event>
    @TypeConverter
    fun toEventList(json: String): List<Event> {
        val type = object : TypeToken<List<Event>>() {}.type
        return gson.fromJson(json, type)
    }
}
