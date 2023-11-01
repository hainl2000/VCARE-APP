package com.example.vcare_app.present.personal.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.response.HistoryAppointment
import com.example.vcare_app.api.api_model.response.HistoryResponse
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel : BaseViewModel() {
    val repository = AppRepository(ApiClient.apiService)
    private val _listAppointment = MutableLiveData<HistoryResponse>()
    val listAppointment: LiveData<HistoryResponse> get() = _listAppointment

    fun getHistory() {
        compositeDisposable.add(repository.getHistoryAppointment()
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe(
                {
                    status.postValue(LoadingStatus.Success)
                    _listAppointment.postValue(it)
                }, {
                    status.postValue(LoadingStatus.Error)
                    errorMsg.postValue(handleError(it))
                }
            ))
    }

    fun searchAppointment(name: String): List<HistoryAppointment> {
        val newList = listAppointment.value?.data?.filter {
            it.hospital.name.toLowerCase().contains(name) || it.department.name.toLowerCase()
                .contains(name)
        }
        return newList ?: emptyList()
    }
}