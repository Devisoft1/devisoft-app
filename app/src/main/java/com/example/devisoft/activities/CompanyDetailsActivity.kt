package com.example.devisoft.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.adapter.CompanyAdapter
import com.example.devisoft.network.RetrofitInstance
import com.example.devisoft.utils.PrefManager
import kotlinx.coroutines.launch

class CompanyDetailsActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var companyAdapter: CompanyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_details)

        // Set up the app bar title
        val appBarTitle: TextView = findViewById(R.id.appBarTitle)
        appBarTitle.text = "Company Details"  // You can dynamically change this based on data

        val logoImage: ImageView = findViewById(R.id.appBarLogo)

        logoImage.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)

            // Add menu item programmatically
            popupMenu.menu.add(0, 1, 0, "Website") // (groupId, itemId, order, title)

            // Handle menu item clicks
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        val url = "https://devisoft.co.in/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        recyclerView = findViewById(R.id.companyRecyclerView)

        // Back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, YearSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val fromDate = intent.getStringExtra("FROM_DATE") ?: ""
        val toDate = intent.getStringExtra("TO_DATE") ?: ""

        Log.d("CompanyDetailsActivity", "Received fromDate: $fromDate, toDate: $toDate")

        // Save the date range into PrefManager for persistence
        if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
            val prefManager = PrefManager(this)
            prefManager.saveDateRange(fromDate, toDate)
            fetchCompanyDetails(fromDate, toDate)
        } else {
            // You can handle invalid date ranges here
            Log.e("CompanyDetailsActivity", "Invalid date range provided.")
        }
    }

    private fun fetchCompanyDetails(fromDate: String, toDate: String) {
        val prefManager = PrefManager(this)
        val accessToken = prefManager.getAccessToken()
        val userId = prefManager.getUserId()

        val tokenHeader = "Bearer $accessToken"
        val userHeader = userId

        Log.d("CompanyDetails", "Token: $tokenHeader, userId: $userHeader")

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getCompanyDetails(
                    token = tokenHeader,
                    userId = userHeader.toString(),
                    fromDate = fromDate,
                    toDate = toDate
                )

                if (response.isSuccessful) {
                    val companies = response.body()
                    Log.d("CompanyDetails", "Fetched companies: $companies")

                    if (!companies.isNullOrEmpty()) {
                        // Set up the adapter with a click handler
                        companyAdapter = CompanyAdapter(companies) { selectedCompany ->
                            val selectedCode = selectedCompany.CompCode

                            // Store the selected company code in local storage
                            prefManager.setCompanyCode(selectedCode ?: "")

                            // Send the company code to the next page (DashboardActivity)
                            val intent = Intent(this@CompanyDetailsActivity, DashboardActivity::class.java)
                            intent.putExtra("COMPANY_CODE", selectedCode) // Pass the company code
                            startActivity(intent)

                            // Optionally finish this activity to prevent returning here
                            finish()
                        }

                        recyclerView.layoutManager = LinearLayoutManager(this@CompanyDetailsActivity)
                        recyclerView.adapter = companyAdapter
                    } else {
                        // Handle the case when no companies are fetched
                        Log.e("CompanyDetails", "No companies found.")
                    }
                } else {
                    Log.e("CompanyDetails", "API Error: ${response.code()} - ${response.message()}")
                    // Handle the error appropriately
                }
            } catch (e: Exception) {
                Log.e("CompanyDetails", "Exception: ${e.message}", e)
                // Handle the exception appropriately
            }
        }
    }
}
