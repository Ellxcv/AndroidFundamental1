package com.example.androidfundamental1.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DetailEvent")
data class DetailEvent(
    val error: Boolean,
    @PrimaryKey val eventId: Int, // Gunakan id dari Events sebagai primary key
    @Embedded val event: Events,
    val message: String
)

