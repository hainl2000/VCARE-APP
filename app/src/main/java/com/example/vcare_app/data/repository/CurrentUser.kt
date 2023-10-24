package com.example.vcare_app.data.repository

import com.example.vcare_app.api.api_model.response.Profile

object CurrentUser {
    private lateinit var _currentUserInformation: Profile
    fun setUserInformation(profile: Profile) {
        _currentUserInformation = profile
    }

    val data get() = _currentUserInformation

}