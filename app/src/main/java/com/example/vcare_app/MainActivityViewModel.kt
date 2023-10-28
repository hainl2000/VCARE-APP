package com.example.vcare_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.base.BaseViewModel

class MainActivityViewModel: BaseViewModel() {
    private val _currentTab = MutableLiveData(0)
    val currentTab:LiveData<Int> get() = _currentTab

    fun changeTab(tab:Int){
        _currentTab.postValue(tab)
    }
}