package com.example.vcare_app.data.repository

import com.example.vcare_app.api.ApiService
import com.example.vcare_app.api.api_model.request.AppointmentRequest
import com.example.vcare_app.api.api_model.request.LoginRequest
import com.example.vcare_app.api.api_model.request.SignUpRequest
import com.example.vcare_app.api.api_model.request.UpdateUserRequest
import com.example.vcare_app.api.api_model.response.LoginResponse
import com.example.vcare_app.model.Doctor
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

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

    fun getDepartmentList(hospitalId: Int) = apiService.getDepartmentList(hospitalId)

    fun getUserProfile() = apiService.getUserProfile()

    fun updateUserProfile(updateUserRequest: UpdateUserRequest) =
        apiService.updateUserProfile(updateUserRequest)

    fun getHospitalList(pageSize: Int, pageIndex: Int) =
        apiService.getHospitalList(pageSize, pageIndex)

    fun uploadImage(file: MultipartBody.Part) = apiService.uploadImage(file)

    fun createAppointment(appointmentRequest: AppointmentRequest) = apiService.createAppointment(appointmentRequest)

    fun getHistoryAppointment() = apiService.getAppointmentHistory()

    fun getAppointmentDetail(id:Int) = apiService.getAppointment(id)
    fun getDoctor() = listOf(
        Doctor(
            "Tran cong huu",
            "https://tamanhhospital.vn/wp-content/uploads/2023/03/tran-ngoc-huu-avt.png",
            "Benh vien E",
            "Ngoai tim mach",
            92,
            5.0
        ),
        Doctor(
            "Tran cong huu",
            "https://tamanhhospital.vn/wp-content/uploads/2023/03/tran-ngoc-huu-avt.png",
            "Benh vien E",
            "Ngoai tim mach",
            92,
            5.0
        ),
        Doctor(
            "Tran cong huu",
            "https://www.fvhospital.com/wp-content/uploads/2018/03/dr-vo-trieu-dat-2020.jpg",
            "Benh vien E",
            "Ngoai tim mach",
            92,
            5.0
        ),
        Doctor(
            "Tran cong huu",
            "https://tamanhhospital.vn/wp-content/uploads/2020/12/pham-huu-tung.png",
            "Benh vien E",
            "Ngoai tim mach",
            92,
            5.0
        ),
    )
}