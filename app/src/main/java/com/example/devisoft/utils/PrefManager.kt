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
        private const val KEY_COMPANY_CODE = "companyCode"  // Added key for CompCode
        private const val KEY_FROM_DATE = "fromDate"  // Key for fromDate
        private const val KEY_TO_DATE = "toDate"      // Key for toDate
        private const val TAG = "PrefManager"
    }

    // Function to save user data
    fun saveUserData(accessToken: String, userId: String, companyName: String) {
        sharedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_USER_ID, userId)
            putString(KEY_COMPANY_NAME, companyName)
            apply()  // apply instead of commit
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

    // Function to save company code (CompCode)
    fun setCompanyCode(compCode: String?) {
        if (!compCode.isNullOrEmpty()) {  // Optional check to ensure not storing empty or null code
            val editor = sharedPreferences.edit()
            editor.putString(KEY_COMPANY_CODE, compCode)  // Save the CompCode
            editor.apply()
            Log.d(TAG, "Saved companyCode=$compCode")
        } else {
            Log.d(TAG, "Invalid companyCode: $compCode")
        }
    }

    // Function to retrieve company code (CompCode)
    fun getCompanyCode(): String? {
        val code = sharedPreferences.getString(KEY_COMPANY_CODE, null)
        Log.d(TAG, "Retrieved companyCode=$code")
        return code
    }

    // Function to save fromDate and toDate
    fun saveDateRange(fromDate: String, toDate: String) {
        sharedPreferences.edit().apply {
            putString(KEY_FROM_DATE, fromDate)
            putString(KEY_TO_DATE, toDate)
            apply()  // apply instead of commit
        }
        Log.d(TAG, "Saved fromDate=$fromDate, toDate=$toDate")
    }

    // Function to retrieve fromDate
    fun getFromDate(): String? {
        val fromDate = sharedPreferences.getString(KEY_FROM_DATE, "")
        Log.d(TAG, "Retrieved fromDate=$fromDate")
        return fromDate
    }

    // Function to retrieve toDate
    fun getToDate(): String? {
        val toDate = sharedPreferences.getString(KEY_TO_DATE, "")
        Log.d(TAG, "Retrieved toDate=$toDate")
        return toDate
    }

    // Function to clear user data
    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_COMPANY_NAME)
            apply()
        }
        Log.d(TAG, "Cleared user data from SharedPreferences")
    }

    // Function to clear all stored data
    fun clear() {
        sharedPreferences.edit().apply {
            clear()
            apply()
        }
        Log.d(TAG, "Cleared all data from SharedPreferences")
    }
}
