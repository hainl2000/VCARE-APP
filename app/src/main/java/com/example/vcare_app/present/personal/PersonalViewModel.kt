package com.example.vcare_app.present.personal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.response.Profile
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PersonalViewModel : BaseViewModel() {
    private val repository = AppRepository(ApiClient.apiService)

    private val _userProfile = MutableLiveData<Profile>()

    val userProfile: LiveData<Profile> get() = _userProfile

    fun getUserProfile() {
        compositeDisposable.add(
            repository.getUserProfile().observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    status.postValue(LoadingStatus.Loading)
                }
                .subscribeOn(Schedulers.io()).subscribe(
                    {
                        Log.i("TAGG",it.toString())
                        _userProfile.postValue(it)
                        status.postValue(LoadingStatus.Success)
                    }, {
                        Log.e("TAGG",it.toString())
                        status.postValue(LoadingStatus.Error)
                    }
                )
        )
    }
}