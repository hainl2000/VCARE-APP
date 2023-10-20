package com.example.vcare_app.model.response

import com.google.gson.annotations.SerializedName

data class Profile(
    val id: Int,
    val phone: String,
    val email: String,
    @SerializedName("full_name") val fullName: String?,
    val avatar: String?,
    val gender: String?,
    val dob: String?,
    val deleted: Boolean,
    @SerializedName("identity_number") val identityNumber: String,
    @SerializedName("social_insurance_number") val socialInsuranceNumber: String,
    @SerializedName("security_key") val securityKey: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("updated_at") val updatedAt: String
)
