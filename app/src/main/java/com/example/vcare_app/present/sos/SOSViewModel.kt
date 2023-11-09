package com.example.vcare_app.present.sos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.model.NearestHospital
import com.example.vcare_app.utilities.Utilities

class SOSViewModel : BaseViewModel() {
    val appRepository = AppRepository(apiService = ApiClient.apiService)
    private val _nearestHospital = MutableLiveData<NearestHospital>()
    val nearestHospital: LiveData<NearestHospital> get() = _nearestHospital

    fun getNearestHospital(currentLatitude: Double, currentLongitude: Double) {
        val listHospital = appRepository.getNearestHospital()
        var nearestHospital: NearestHospital = listHospital[0]
        var minDistance = Double.MAX_VALUE
        for (location in listHospital) {
            val distance = Utilities.haversineDistance(
                currentLatitude,
                currentLongitude,
                location.latitude,
                location.longitude
            )
            if (minDistance >= distance) {
                minDistance = distance
                nearestHospital = location
            }
        }
        _nearestHospital.postValue(nearestHospital)
    }
}