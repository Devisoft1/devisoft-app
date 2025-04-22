package com.example.devisoft.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.adapters.CompanyAdapter
import com.example.devisoft.models.Company
import com.example.devisoft.network.RetrofitInstance
import com.example.devisoft.utils.PrefManager
import kotlinx.coroutines.launch

class CompanyDetailsActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var companyAdapter: CompanyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_details)

        recyclerView = findViewById(R.id.companyRecyclerView)

        val fromDate = intent.getStringExtra("FROM_DATE") ?: ""
        val toDate = intent.getStringExtra("TO_DATE") ?: ""

        Log.d("CompanyDetailsActivity", "Received fromDate: $fromDate, toDate: $toDate")

        if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
            fetchCompanyDetails(fromDate, toDate)
        } else {
            Toast.makeText(this, "Invalid date range", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCompanyDetails(fromDate: String, toDate: String) {
        // Get the accessToken and userId from PrefManager
        val prefManager = PrefManager(this)
        val accessToken = prefManager.getAccessToken()
        val userId = prefManager.getUserId()

        // Prepare the headers as per your request:
        val tokenHeader = "Bearer $accessToken"  // Send as 'Bearer <accessToken>'
        val userHeader = userId  // Just pass userId directly (no prefix)

        Log.d("CompanyDetails", "Token: $tokenHeader, userId: $userHeader")

        lifecycleScope.launch {
            try {
                // Make API call, passing both headers
                val response = RetrofitInstance.api.getCompanyDetails(
                    token = tokenHeader,
                    userId = userHeader.toString(),  // userId directly, no extra prefix
                    fromDate = fromDate,
                    toDate = toDate
                )

                if (response.isSuccessful) {
                    val companies = response.body()
                    Log.d("CompanyDetails", "Fetched companies: $companies")

                    if (!companies.isNullOrEmpty()) {
                        companyAdapter = CompanyAdapter(companies)
                        recyclerView.layoutManager = LinearLayoutManager(this@CompanyDetailsActivity)
                        recyclerView.adapter = companyAdapter
                    } else {
                        Toast.makeText(this@CompanyDetailsActivity, "No company data found.", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Log.e("CompanyDetails", "API Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@CompanyDetailsActivity, "Failed to fetch company data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CompanyDetails", "Exception: ${e.message}", e)
                Toast.makeText(this@CompanyDetailsActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
