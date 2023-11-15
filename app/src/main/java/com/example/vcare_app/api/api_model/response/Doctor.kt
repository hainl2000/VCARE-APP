package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class Doctor(
    val id: Int,
    @SerializedName("hospital_id") val hospitalId: Int,
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("service_id") val serviceId: Int?,
    @SerializedName("practicing_certificate_code") val practicingCertificateCode: String,
    val code: String,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val avatar: String,
    val gender: Any?,  // Replace 'Any?' with the actual type if possible
    @SerializedName("role_id") val roleId: Int,
    val deleted: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("external_code") val externalCode: String,
    @SerializedName("change_history") val changeHistory: List<Any>,  // Replace 'Any' with the actual type if possible
    @SerializedName("security_key") val securityKey: String
)
