package com.example.androidfundamental1.util

// Enum untuk mendefinisikan status dari operasi
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

// Kelas Resource untuk membungkus data dan status dari operasi
data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(msg: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data)
        }
    }
}
