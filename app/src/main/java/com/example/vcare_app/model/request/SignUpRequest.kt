package com.example.vcare_app.model.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val email: String,
    val phone: String,
    @SerializedName("social_insurance_number") val socialInsuranceNumber: String,
    @SerializedName("identity_number") val identityNumber: String,
    val password: String
)
