package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class AppointmentDetailResponse(
    val id: Int,
    @SerializedName("hospital_id") val hospitalId: Int,
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("doctor_id") val doctorId: Int,
    @SerializedName("external_code") val externalCode: String,
    val order:Int,
    @SerializedName("patient_information") val patientInformation: PatientInformation,
    @SerializedName("medical_condition") val medicalCondition: String,
    val time: String,
    @SerializedName("time_in_string") val timeInString: String,
    val finished: Boolean,
    val conclude: String,
    val note: String,
    val medicine: String,
    val fee: Int,
    @SerializedName("fee_paid") val feePaid: Boolean,
    val services: List<ServiceDetail>,
    val doctor: Doctor,
    val hospital: HospitalOnlyNameInforResponse,
    val department: DepartmentOnlyNameResponse,
    @SerializedName("status") val status: String,
    @SerializedName("suggest_time") val suggestTime: String,
    @SerializedName("re_examination") val reExamination:String?,
    @SerializedName("periodi_examination") val periodiExamination:String?,
    @SerializedName("services_result") val servicesResult: List<ServicesResult>
)





