package com.example.vcare_app.present.personal.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.response.HistoryAppointment
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel : BaseViewModel() {
    val repository = AppRepository(ApiClient.apiService)
    private val _listAppointment = MutableLiveData<List<HistoryAppointment>>()
    val listAppointment: LiveData<List<HistoryAppointment>> get() = _listAppointment

    private val _loadMoreStatus = MutableLiveData(false)
    val loadMoreStatus: LiveData<Boolean> get() = _loadMoreStatus

    var isLastPage = false

    private val pageSize = 10
    private var pageIndex = 1

    fun getHistory(listPageSize: Int = pageSize, listPageIndex: Int = pageIndex) {
        compositeDisposable.add(repository.getHistoryAppointment(
            pageSize = listPageSize,
            pageIndex = listPageIndex
        )
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe(
                {
                    status.postValue(LoadingStatus.Success)
                    _listAppointment.postValue(it.data)
                }, {
                    status.postValue(LoadingStatus.Error)
                    errorMsg.postValue(handleError(it))
                }
            ))
    }

    fun loadMoreHistory() {
        pageIndex += 1
        compositeDisposable.add(repository.getHistoryAppointment(pageSize, pageIndex)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                _loadMoreStatus.postValue(true)
            }.subscribe(
                {
                    _loadMoreStatus.postValue(false)
                    val currentList = _listAppointment.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(it.data)
                    if (currentList.size >= it.total) {
                        isLastPage = true
                    }
                    _listAppointment.postValue(currentList)
                }, {
                    _loadMoreStatus.postValue(false)
                    errorMsg.postValue(handleError(it))
                }
            ))
    }

    fun searchAppointment(name: String): List<HistoryAppointment> {
        val newList = listAppointment.value?.filter {
            it.hospital.name.toLowerCase().contains(name) || it.department.name.toLowerCase()
                .contains(name)
        }
        return newList ?: emptyList()
    }
}