package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class HospitalService(
    @SerializedName("id") val serviceId: Int,
    @SerializedName("service_id") val serviceId2: Int,
    val name: String,
    val fee: Int,
    val room: String
)
