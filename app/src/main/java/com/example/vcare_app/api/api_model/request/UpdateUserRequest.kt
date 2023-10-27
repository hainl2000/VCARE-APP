package com.example.vcare_app.api.api_model.request

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("full_name") val fullName: String,
    val avatar: String?,
    val gender: Boolean,
    val dob: String,
    @SerializedName("identity_number") val identityNumber: String,
    @SerializedName("social_insurance_number") val socialInsuranceNumber: String,
    val address: String
)