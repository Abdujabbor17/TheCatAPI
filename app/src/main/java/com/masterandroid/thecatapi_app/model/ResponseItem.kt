package com.masterandroid.thecatapi_app.model

data class ResponseItem(
    val breeds: List<Any>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)