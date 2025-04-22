package com.example.devisoft.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val KEY_USER_ID = "userId"
        private const val KEY_COMPANY_NAME = "companyName"
        private const val TAG = "PrefManager"
    }

    // Function to save user data
    fun saveUserData(accessToken: String, userId: String, companyName: String) {
        sharedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_USER_ID, userId)
            putString(KEY_COMPANY_NAME, companyName)
            apply()
        }
        Log.d(TAG, "Saved accessToken=$accessToken, userId=$userId, companyName=$companyName")
    }

    // Function to retrieve access token
    fun getAccessToken(): String? {
        val token = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        Log.d(TAG, "Retrieved accessToken=$token")
        return token
    }

    // Function to retrieve user ID
    fun getUserId(): String? {
        val id = sharedPreferences.getString(KEY_USER_ID, null)
        Log.d(TAG, "Retrieved userId=$id")
        return id
    }

    // Function to retrieve company name
    fun getCompanyName(): String? {
        val name = sharedPreferences.getString(KEY_COMPANY_NAME, null)
        Log.d(TAG, "Retrieved companyName=$name")
        return name
    }

    // Function to clear stored data
    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
        Log.d(TAG, "Cleared all user data from SharedPreferences")
    }
}
