package com.example.androidfundamental1.api

import com.example.androidfundamental1.models.BangkitList
import com.example.androidfundamental1.models.DetailEvent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BangkitAPI {

    // Mendapatkan list event yang aktif (akan datang)
    @GET("/events")
    suspend fun getUpcomingEvents(
        @Query("active") active: Int = 1,    // active=1 berarti event yang akan datang
        @Query("limit") limit: Int = 40      // Opsional, limit hasil
    ): BangkitList

    // Mendapatkan list event yang sudah selesai
    @GET("/events")
    suspend fun getPastEvents(
        @Query("active") active: Int = 0,    // active=0 berarti event yang sudah selesai
        @Query("limit") limit: Int = 40      // Opsional, limit hasil
    ): BangkitList

    // Mencari event berdasarkan keyword
    @GET("/events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,   // active=-1 untuk pencarian event aktif dan selesai
        @Query("q") query: String,           // Keyword pencarian
        @Query("limit") limit: Int = 40      // Opsional, limit hasil
    ): BangkitList

    // Mendapatkan detail event berdasarkan ID
    @GET("/events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int                 // ID event spesifik
    ): DetailEvent

}
