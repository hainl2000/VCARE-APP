package com.example.vcare_app.api.api_model

import com.google.gson.annotations.SerializedName

data class HealthStatus(
    val height: Int,
    val weight: Int,
    @SerializedName("blood_type")
    val bloodType: String,
    @SerializedName("blood_pressure")
    val bloodPressure: String
)
