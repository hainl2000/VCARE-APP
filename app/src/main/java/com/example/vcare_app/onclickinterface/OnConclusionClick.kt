package com.example.vcare_app.onclickinterface

import com.example.vcare_app.api.api_model.response.ConclusionResponse

interface OnConclusionClick {
    fun onConclusionClick(conclusionResponse: ConclusionResponse)
}