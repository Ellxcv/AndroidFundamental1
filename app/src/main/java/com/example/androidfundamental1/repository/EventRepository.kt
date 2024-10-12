package com.example.androidfundamental1.repository

import com.example.androidfundamental1.api.BangkitAPI
import com.example.androidfundamental1.db.EventDatabase
import com.example.androidfundamental1.models.BangkitList
import com.example.androidfundamental1.models.DetailEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(
    private val bangkitAPI: BangkitAPI,
    private val eventDatabase: EventDatabase
) {

    // Mendapatkan semua event yang akan datang
    suspend fun getUpcomingEvents(active: Int = 1): BangkitList? {
        return fetchEvents { bangkitAPI.getUpcomingEvents(active) }
    }

    // Mendapatkan semua event yang sudah selesai
    suspend fun getPastEvents(active: Int = 0): BangkitList? {
        return fetchEvents { bangkitAPI.getPastEvents(active) }
    }

    // Mencari event berdasarkan keyword untuk past events
    suspend fun searchPastEvents(query: String, active: Int = 0): BangkitList? {
        return fetchEvents { bangkitAPI.searchEvents(active, query) }
    }


    // Mendapatkan detail event berdasarkan ID
    suspend fun getEventDetail(eventId: Int): DetailEvent? {
        // Coba ambil data dari Room Database terlebih dahulu
        var detailEvent = eventDatabase.getEventDao().getDetailEventById(eventId)

        // Jika data tidak ditemukan di database, ambil dari API
        if (detailEvent == null) {
            detailEvent = withContext(Dispatchers.IO) {
                try {
                    bangkitAPI.getEventDetail(eventId)
                } catch (e: Exception) {
                    null
                }
            }

            // Jika data berhasil diambil dari API, simpan ke database
            detailEvent?.let {
                eventDatabase.getEventDao().upsert(it)
            }
        }
        return detailEvent
    }

    // Fungsi privat untuk mengambil data event dengan penanganan error
    private suspend fun fetchEvents(fetch: suspend () -> BangkitList?): BangkitList? {
        return withContext(Dispatchers.IO) {
            try {
                fetch()
            } catch (e: Exception) {
                null // Atau bisa mengelola error dengan cara lain
            }
        }
    }


}
