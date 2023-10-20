package com.example.vcare_app.repository

import com.example.vcare_app.api.ApiService
import com.example.vcare_app.model.request.LoginRequest
import com.example.vcare_app.model.request.SignUpRequest
import com.example.vcare_app.model.response.LoginResponse
import io.reactivex.rxjava3.core.Single

class AppRepository(private val apiService: ApiService) {
    fun loginUser(userName: String, password: String): Single<LoginResponse> {
        return apiService.loginUser(LoginRequest(userName, password))
    }

    fun signUp(
        email: String,
        phone: String,
        sin: String,
        idn: String,
        password: String
    ): Single<LoginResponse> {
        return apiService.registerUser(SignUpRequest(email, phone, sin, idn, password))
    }
}