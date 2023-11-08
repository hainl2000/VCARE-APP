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
import java.util.Locale

class HospitalBookingViewModel : BaseViewModel() {
    private val repository = AppRepository(ApiClient.apiService)
    private val _listHospital = MutableLiveData<List<Hospital>>(emptyList())
    val listHospital: LiveData<List<Hospital>> get() = _listHospital

    private val _loadMoreStatus = MutableLiveData(false)
    val loadMoreStatus: LiveData<Boolean> get() = _loadMoreStatus

    private var pageIndex = 1
    private val pageSize = 10
    var totalcount =0
    fun getHospitalList(pageSize: Int = 10, pageIndex: Int = 1) {
        compositeDisposable.add(repository.getHospitalList(pageSize, pageIndex)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe({
                status.postValue(LoadingStatus.Success)
                _listHospital.postValue(it.hospitals)
                totalcount = it.total
            }, {
                status.postValue(LoadingStatus.Success)
                errorMsg.postValue(it.message)
            })
        )
    }

    fun loadMoreHospital() {
        pageIndex++
        compositeDisposable.add(repository.getHospitalList(pageSize, pageIndex)
            .doOnSubscribe {
                _loadMoreStatus.postValue(true)
            }
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                {
                    _loadMoreStatus.postValue(false)
                    val currentList = _listHospital.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(it.hospitals)
                    _listHospital.postValue(currentList)
                }, {
                    _loadMoreStatus.postValue(false)
                    errorMsg.postValue(handleError(it))
                }
            ))
    }


    fun searchHospital(name: String): List<Hospital> {
        val newList = _listHospital.value?.filter {
            it.name.lowercase(Locale.ROOT).contains(name)
        }
        return newList ?: emptyList()
    }

}