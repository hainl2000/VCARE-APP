package com.example.vcare_app.api.api_model.request

import com.google.gson.annotations.SerializedName

data class AppointmentRequest(
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("hospital_id") val hospitalId: Int,
    @SerializedName("medical_condition") val medicalCondition: String,
    @SerializedName("time_in_string") val timeInString: String,
    @SerializedName("identity_number") val identityNumber: String,
    @SerializedName("social_insurance_number") val socialInsuranceNumber: String,
    val hour: Int?=null,
    val address: String
)