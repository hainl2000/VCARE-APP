package com.example.vcare_app.api

import com.example.vcare_app.model.request.LoginRequest
import com.example.vcare_app.model.request.SignUpRequest
import com.example.vcare_app.model.response.LoginResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("auth/register")
    fun registerUser(@Body signUpRequest: SignUpRequest): Single<LoginResponse>
}