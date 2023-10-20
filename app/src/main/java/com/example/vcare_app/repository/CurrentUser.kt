package com.example.vcare_app.repository

import com.example.vcare_app.model.response.Profile

object CurrentUser {
    private lateinit var _currentUserInformation: Profile
    fun setUserInformation(profile: Profile) {
        _currentUserInformation = profile
    }

    val data get() = _currentUserInformation

}