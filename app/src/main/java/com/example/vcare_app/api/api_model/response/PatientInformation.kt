package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName
data class PatientInformation(
    val email: String,
    val phone: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("identity_number") val identityNumber: String,
    @SerializedName("social_insurance_number") val socialInsuranceNumber: String
)