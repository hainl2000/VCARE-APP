package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class Department(
    val id: Int,
    val name: String,
    @SerializedName("hospital_id") val hospitalID: Int
)