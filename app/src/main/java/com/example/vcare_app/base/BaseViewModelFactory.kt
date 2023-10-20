package com.example.vcare_app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseViewModelFactory<T>(private val argument: T? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseViewModel::class.java))
            return BaseViewModel(argument) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}