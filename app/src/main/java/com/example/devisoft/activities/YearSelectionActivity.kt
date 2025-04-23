package com.example.devisoft.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_selection)

        // Set title and logo
        val titleText: TextView = findViewById(R.id.appBarTitle)
        titleText.text = "Select Financial Year"  // Set dynamic title

        val logoImage: ImageView = findViewById(R.id.appBarLogo)
        logoImage.setOnClickListener {
            val url = "https://devisoft.co.in/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Back Button Logic
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val prefManager = PrefManager(this)
            prefManager.clear()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.financialYearRecyclerView)

        // Fetch financial years from API
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
        financialYearAdapter = FinancialYearAdapter(financialYears ?: emptyList()) { selectedYear ->

            val fyear = selectedYear.fyear // "01/04/2024 TO 31/03/2025"
            val dates = fyear.split(" TO ")
            val fromDate = dates[0]     // "01/04/2024"
            val toDate = dates[1]       // "31/03/2025"

            val intent = Intent(this, CompanyDetailsActivity::class.java)
            intent.putExtra("FROM_DATE", fromDate)
            intent.putExtra("TO_DATE", toDate)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = financialYearAdapter
    }
}
