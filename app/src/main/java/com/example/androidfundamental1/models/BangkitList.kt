package com.example.androidfundamental1.models

data class BangkitList(
    val error: Boolean,
    val listEvents: MutableList<Events>,
    val message: String
)