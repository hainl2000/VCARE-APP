package com.example.vcare_app.model

data class NearestHospital(
    val hospitalName: String,
    val hospitalAddress: String,
    val hospitalNumber: String,
    val latitude: Double,
    val longitude: Double
)
