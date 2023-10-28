package com.example.vcare_app.present.personal.editpersonal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.request.UpdateUserRequest
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class EditPersonalFragmentViewModel : BaseViewModel() {
    private val repository = AppRepository(ApiClient.apiService)

    private val _imgUrl = MutableLiveData<String>()
    val imgUrl: LiveData<String> get() = _imgUrl

    private val _updateSuccess = MutableLiveData(false)
    val updateSuccess:LiveData<Boolean> get() = _updateSuccess

    fun uploadImage(file: File) {
        val requestFile = RequestBody.create(MultipartBody.FORM, file)
        Log.e("Error", "${file.name}")

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        compositeDisposable.add(repository.uploadImage(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe(
                {
                    status.postValue(LoadingStatus.Success)
                    _imgUrl.postValue(it.fileName)
                }, {
                    errorMsg.postValue(it.toString())
                    Log.e("Error", "${it.message}")
                    status.postValue(LoadingStatus.Error)
                }
            ))
    }

    fun updateUser(updateUserRequest: UpdateUserRequest) {
        Log.e("Error:","${updateUserRequest.avatar} ,${updateUserRequest.gender}, ${updateUserRequest.dob}")
        compositeDisposable.add(repository.updateUserProfile(updateUserRequest)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.doOnError {
                _updateSuccess.postValue(false)
                status.postValue(LoadingStatus.Error)
                Log.e("Error: ", it.toString())
            }
            .subscribe(
                {
                    _updateSuccess.postValue(true)
                    status.postValue(LoadingStatus.Success)
                }, {
                    _updateSuccess.postValue(false)
                    status.postValue(LoadingStatus.Error)
                    Log.e("Error: ", it.toString())
                }
            )
        )
    }
}