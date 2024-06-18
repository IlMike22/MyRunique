package com.example.run.presentation.overview.models

data class RunUi(
    val id: String,
    val duration: String,
    val dateTime: String,
    val distance: String,
    val averageSpeed: String,
    val maxSpeed: String,
    val pace: String,
    val totalElevation: String,
    val mapPictureUrl: String?
)
