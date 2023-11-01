package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class HospitalDetailResponse(
    val id: Int,
    val name: String,
    val address: String,
    val image: String?,
    val information: String?,
    val email: String,
    val phone: String,
    val password: String,
    @SerializedName("security_key") val securityKey: String,
    @SerializedName("created_by_id") val createdById: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("change_history") val changeHistory: List<String>,
    @SerializedName("hospital_department") val hospitalDepartment: List<Department>
)