package com.example.vcare_app.present.booking.hospitalbooking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.response.Hospital
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HospitalBookingViewModel : BaseViewModel() {
    private val repository = AppRepository(ApiClient.apiService)
    private val _listHospital = MutableLiveData<List<Hospital>>(emptyList())
    val listHospital: LiveData<List<Hospital>> get() = _listHospital
    fun getHospitalList(pageSize: Int = 10, pageIndex: Int = 1) {
        compositeDisposable.add(repository.getHospitalList(pageSize, pageIndex)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
            status.postValue(LoadingStatus.Loading)
        }.subscribe({
            status.postValue(LoadingStatus.Success)
            _listHospital.postValue(it.hospitals)
        }, {
            status.postValue(LoadingStatus.Success)
            errorMsg.postValue(it.message)
        })
        )
    }
}