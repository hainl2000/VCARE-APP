package com.example.vcare_app.data.repository

import com.example.vcare_app.api.ApiService
import com.example.vcare_app.api.api_model.request.AppointmentRequest
import com.example.vcare_app.api.api_model.request.LoginRequest
import com.example.vcare_app.api.api_model.request.SignUpRequest
import com.example.vcare_app.api.api_model.request.UpdateUserRequest
import com.example.vcare_app.api.api_model.response.ConclusionResponse
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

    fun createAppointment(appointmentRequest: AppointmentRequest) =
        apiService.createAppointment(appointmentRequest)

    fun getHistoryAppointment(pageSize: Int, pageIndex: Int) =
        apiService.getAppointmentHistory(pageSize, pageIndex)

    fun getAppointmentDetail(id: Int) = apiService.getAppointment(id)

    fun getConclusion() = listOf(
        ConclusionResponse(
            "mau abc",
            "https://mekoong.com/wp-content/uploads/2022/12/Hinh-nen-linh-ho-tro-thoi-chien-4k.png"
        ),
        ConclusionResponse(
            "mau abc",
            "https://medlatec.vn/ImagePath/images/20200324/20200324_chup-x-quang-dau-la-mot-phuong-phap-toi-uu-de-phat-hien-nhung-benh-ve-hop-so.jpg"
        ),
        ConclusionResponse(
            "mau abc",
            "https://png.pngtree.com/thumb_back/fw800/background/20210316/pngtree-vertical-version-of-pink-photography-picture-spring-cherry-blossoms-image_586850.jpg"
        ),
        ConclusionResponse(
            "mau abc",
            "data:image/jpeg;base64,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"
        )

    )

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