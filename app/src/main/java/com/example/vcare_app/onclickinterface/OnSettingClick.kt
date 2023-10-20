package com.example.vcare_app.onclickinterface

import com.example.vcare_app.model.SettingsItem
import okhttp3.internal.http2.Settings

interface OnSettingClick {
    fun onSettingClick(settings: SettingsItem)
}