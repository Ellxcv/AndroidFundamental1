package com.example.androidfundamental1.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidfundamental1.models.DetailEvent
import com.example.androidfundamental1.models.Events

@Dao
interface DetailEventDAO {

    // Menggunakan DetailEvent sebagai parameter karena kita ingin menyimpan DetailEvent
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(detailEvent: DetailEvent): Long // Mengembalikan ID dari entitas yang disimpan

    // Mengambil DetailEvent berdasarkan ID
    @Query("SELECT * FROM DetailEvent WHERE id = :eventId LIMIT 1")
    suspend fun getDetailEventById(eventId: Int): DetailEvent? // Mengambil DetailEvent dengan ID tertentu

    // Mengambil semua Events dan mengembalikannya sebagai LiveData
    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Events>>

    // Menghapus Events berdasarkan objek Events
    @Delete
    suspend fun deleteEvent(events: Events)
}
