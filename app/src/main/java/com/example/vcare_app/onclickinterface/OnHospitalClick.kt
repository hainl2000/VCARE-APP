package com.example.vcare_app.onclickinterface

import com.example.vcare_app.api.api_model.response.Hospital

interface OnHospitalClick {
    fun onHospitalItemClick(hospital: Hospital)
}