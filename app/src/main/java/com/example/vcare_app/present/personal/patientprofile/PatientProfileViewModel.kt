package com.example.vcare_app.present.personal.patientprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PatientProfileViewModel : BaseViewModel() {

    val repository = AppRepository(ApiClient.apiService)
    private val _listData = MutableLiveData<List<String>>(emptyList())
    val listData: LiveData<List<String>> get() = _listData

    fun getPatientProfile() {
        compositeDisposable.add(repository.getPatientProfile().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                status.value = LoadingStatus.Loading
            }.subscribe(
                { rs ->
                    _listData.value = rs.data
                    errorMsg.value = ""
                    status.value = LoadingStatus.Success
                }, {
                    errorMsg.value = handleError(it)
                    status.value = LoadingStatus.Error
                }
            ))
    }
}