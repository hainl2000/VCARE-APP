package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class AppointmentDetailResponse(
    val id: Int,
    @SerializedName("hospital_id") val hospitalId: Int,
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("doctor_id") val doctorId: Int?,
    @SerializedName("external_code") val externalCode: String,
    @SerializedName("patient_information") val patientInformation: PatientInformation,
    @SerializedName("medical_condition") val medicalCondition: String,
    val time: String,
    @SerializedName("time_in_string") val timeInString: String,
    val finished: Boolean,
    val conclude: String?,
    val note: String?,
    val medicine: String?,
    val fee: Double?,
    @SerializedName("fee_paid") val feePaid: Boolean,
    val services: List<String>,
    val hospital: HospitalDetailResponse,
    val department: DepartmentOnlyNameResponse,
    val doctor: String?,
)