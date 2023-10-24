package com.example.vcare_app.api.api_model.response
data class LoginResponse(
    val token: String,
    val requestToken: String,
    val profile: Profile
)