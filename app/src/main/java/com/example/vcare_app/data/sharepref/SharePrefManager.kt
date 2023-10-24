package com.example.vcare_app.data.sharepref

import android.content.Context
import android.content.SharedPreferences


object SharePrefManager {
    private const val SHARE_PREF_NAME = "MyPref"
    private lateinit var sharePreference: SharedPreferences
    fun init(context: Context) {
        sharePreference = context.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveStatusSave(isSaved: Boolean) {
        val editor = sharePreference.edit()
        editor.putBoolean(SharePrefItem.LoginSaveStatus.name, isSaved)
        editor.apply()
    }

    fun getStatusSave() = sharePreference.getBoolean(SharePrefItem.LoginSaveStatus.name, false)

    fun saveEmail(email: String) {
        val editor = sharePreference.edit()
        editor.putString(SharePrefItem.Email.name, email)
        editor.apply()
    }

    fun getEmail() = sharePreference.getString(SharePrefItem.Email.name, "") ?: ""

    fun savePassword(password: String) {
        val editor = sharePreference.edit()
        editor.putString(SharePrefItem.Password.name, password)
        editor.apply()
    }

    fun getPassword() = sharePreference.getString(SharePrefItem.Password.name, "") ?: ""

    fun saveAccessToken(accessToken: String) {
        val editor = sharePreference.edit()
        editor.putString(SharePrefItem.AccessToken.name, accessToken)
        editor.apply()
    }

    fun getAccessToken() = sharePreference.getString(SharePrefItem.AccessToken.name, "") ?: ""

    fun deleteAllSavedData() {
        val editor = sharePreference.edit()
        editor.clear()
        editor.apply()
    }
}