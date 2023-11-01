package com.example.vcare_app.api.api_model.response



data class HistoryResponse(
    val data: List<HistoryAppointment>,
    val total: Int
)
