package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class AppointmentResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("hospital_id") val hospitalId: Int,
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("doctor_id") val doctorId: Int?,
    @SerializedName("external_code") val externalCode: String,
    @SerializedName("patient_information") val patientInformation: Map<String, Any>,
    @SerializedName("medical_condition") val medicalCondition: String,
    @SerializedName("time") val time: String,
    @SerializedName("time_in_string") val timeInString: String,
    @SerializedName("finished") val finished: Boolean,
    @SerializedName("conclude") val conclude: String?,
    @SerializedName("note") val note: String?,
    @SerializedName("medicine") val medicine: String?,
    @SerializedName("fee") val fee: Double?,
    @SerializedName("fee_paid") val feePaid: Boolean,
    @SerializedName("suggest_time") val suggestTime:String,
)