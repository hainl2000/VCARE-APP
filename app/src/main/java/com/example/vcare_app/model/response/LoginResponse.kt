package com.example.vcare_app.model.response
data class LoginResponse(
    val token: String,
    val requestToken: String,
    val profile: Profile
)