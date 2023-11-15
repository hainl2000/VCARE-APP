package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class ServiceDetail(
    @SerializedName("appointment_id") val appointmentId: Int,
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("doctor_id") val doctorId: Int?,
    @SerializedName("result_image") val resultImage: List<String>,
    val doctor: Any?,  // Replace 'Any?' with the actual type if possible
    val service: HospitalService
)