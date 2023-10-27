package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class HospitalListResponse(
    @SerializedName("data") val hospitals: List<Hospital>,
    @SerializedName("total") val total: Int
)