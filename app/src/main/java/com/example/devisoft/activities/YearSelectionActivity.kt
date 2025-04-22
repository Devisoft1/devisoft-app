package com.example.devisoft.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.adapters.FinancialYearAdapter
import com.example.devisoft.models.FinancialYear
import com.example.devisoft.network.RetrofitInstance

import com.example.devisoft.utils.PrefManager
import kotlinx.coroutines.launch
import retrofit2.HttpException


class YearSelectionActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var financialYearAdapter: FinancialYearAdapter
    private lateinit var selectYearButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_selection)

        recyclerView = findViewById(R.id.financialYearRecyclerView)

        val prefManager = PrefManager(this)
        val accessToken = prefManager.getAccessToken()
        val userId = prefManager.getUserId()

        Log.d("YearSelectionActivity", "Token from Prefs: $accessToken")
        Log.d("YearSelectionActivity", "UserID from Prefs: $userId")

        if (!accessToken.isNullOrEmpty() && !userId.isNullOrEmpty()) {
            fetchFinancialYears(accessToken, userId)
        } else {
            Toast.makeText(this, "Token or User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFinancialYears(accessToken: String, userId: String) {
        lifecycleScope.launch {
            try {
                val tokenHeader = "Bearer $accessToken"

                val response = RetrofitInstance.api.getFinancialYears(tokenHeader, userId)

                if (response.isSuccessful) {
                    val financialYears = response.body()
                    Log.d("YearSelection", "Financial years: $financialYears")

                    // Set up RecyclerView with financial years
                    setupRecyclerView(financialYears)
                } else {
                    Log.e("YearSelection", "Failed: ${response.code()} ${response.message()}")
                    Toast.makeText(applicationContext, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }

            } catch (e: HttpException) {
                Log.e("YearSelection", "HTTP Exception: ${e.message()}")
                Toast.makeText(applicationContext, "HTTP error occurred", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("YearSelection", "Exception: ${e.message}")
                Toast.makeText(applicationContext, "Unexpected error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(financialYears: List<FinancialYear>?) {
        financialYearAdapter = FinancialYearAdapter(financialYears ?: emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = financialYearAdapter
    }


}
