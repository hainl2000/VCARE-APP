package com.example.vcare_app.onclickinterface

import com.example.vcare_app.api.api_model.response.Department

interface OnDepartmentItemClick {
    fun onDepartmentClick(department: Department)
}