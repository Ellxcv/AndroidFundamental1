package com.example.androidfundamental1.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "events"
)

data class Events(
    @PrimaryKey
    val id: Int,
    val beginTime: String,
    val category: String,
    val cityName: String,
    val description: String,
    val endTime: String,
    val imageLogo: String,
    val link: String,
    val mediaCover: String,
    val name: String,
    val ownerName: String,
    val quota: Int,
    val registrants: Int,
    val summary: String
): Serializable