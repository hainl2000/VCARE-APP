package com.example.vcare_app.onclickinterface

import com.example.vcare_app.api.api_model.response.HistoryAppointment

interface OnAppointmentClick {
    fun onAppointmentClick(historyAppointment: HistoryAppointment)
}