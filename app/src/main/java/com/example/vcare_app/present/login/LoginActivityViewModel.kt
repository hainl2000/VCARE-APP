package com.example.vcare_app.present.login

import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.base.BaseViewModel


class LoginActivityViewModel : BaseViewModel() {
    private val currentIndex = MutableLiveData(0)
    val getCurrentIndex get() = currentIndex
    fun setCurrentIndex(index: Int) {
        currentIndex.postValue(index)
    }
}