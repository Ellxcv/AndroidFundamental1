package com.example.androidfundamental1.api

// Tambahkan import untuk BangkitAPI jika belum ada
import com.example.androidfundamental1.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance { // Ganti class menjadi object agar instance tunggal
    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // Atur logging level untuk debugging

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL) // Pastikan BASE_URL benar
            .addConverterFactory(GsonConverterFactory.create()) // Tambahkan Gson sebagai converter
            .client(client) // Tambahkan HTTP client
            .build()
    }

    // Instansiasi API
    val api: BangkitAPI by lazy {
        retrofit.create(BangkitAPI::class.java)
    }
}
