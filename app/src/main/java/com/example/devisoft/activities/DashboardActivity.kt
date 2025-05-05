package com.example.devisoft.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.devisoft.R
import com.example.devisoft.network.RetrofitInstance
import com.example.devisoft.utils.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var todaySalesTextView: TextView
    private lateinit var todayPurchaseTextView: TextView
    private lateinit var prefManager: PrefManager
    private lateinit var dateFilterSpinner: Spinner
    private lateinit var customDateLayout: LinearLayout
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button

    private var startDate: String? = null
    private var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefManager = PrefManager(this)

        findViewById<TextView>(R.id.appBarTitle)?.text = "Dashboard"
        todaySalesTextView = findViewById(R.id.today_sales_text_view)
        todayPurchaseTextView = findViewById(R.id.today_purchase_text_view)
        dateFilterSpinner = findViewById(R.id.dateFilterSpinner)
        customDateLayout = findViewById(R.id.customDateLayout)
        startDateButton = findViewById(R.id.selectStartDateButton)
        endDateButton = findViewById(R.id.selectEndDateButton)

        // Set up the Spinner (Dropdown) with date filter options
        val dateFilterOptions = arrayOf("Today", "Yesterday", "This Week", "Last Week", "This Month", "Last Month", "This Year", "Custom")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dateFilterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val appBarTitle: TextView = findViewById(R.id.appBarTitle)
        appBarTitle.text = "Dashboard"

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
        dateFilterSpinner.adapter = adapter

        dateFilterSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = parent?.getItemAtPosition(position).toString()
                if (selectedFilter == "Custom") {
                    customDateLayout.visibility = LinearLayout.VISIBLE
                } else {
                    customDateLayout.visibility = LinearLayout.GONE
                    fetchTotalSales(selectedFilter)
                    fetchTotalPurchase(selectedFilter)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected
            }
        })


        startDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        endDateButton.setOnClickListener {
            showDatePickerDialog(false)
        }

        todaySalesTextView.setOnClickListener {
            startActivity(Intent(this, TodaySalesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.today_sales).setOnClickListener {
            startActivity(Intent(this, TodaySalesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.today_purchase).setOnClickListener {
            startActivity(Intent(this, TodayPurchaseActivity::class.java))
        }


        // Default fetch call with "Today" filter on activity creation
        fetchTotalSales("Today")
        fetchTotalPurchase("Today")
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = "${month + 1}/$dayOfMonth/$year"
            if (isStartDate) {
                startDate = selectedDate
                startDateButton.text = "Start Date: $selectedDate"
            } else {
                endDate = selectedDate
                endDateButton.text = "End Date: $selectedDate"
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }
    private fun fetchTotalSales(dateFilter: String) {
        val compCode = prefManager.getCompanyCode()
        val accessToken = prefManager.getAccessToken()
        val userId = prefManager.getUserId()

        if (compCode.isNullOrEmpty() || accessToken.isNullOrEmpty() || userId.isNullOrEmpty()) {
            todaySalesTextView.text = "Missing credentials"
            Log.e("DashboardActivity", "Missing required data from PrefManager")
            return
        }

        val bearerToken = "Bearer $accessToken"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (dateFilter == "Custom" && !startDate.isNullOrEmpty() && !endDate.isNullOrEmpty()) {
                    RetrofitInstance.api.getTotalSalesWithHeaders(
                        accessToken = bearerToken,
                        compCode = compCode,
                        dateFilter = "custom",
                        userId = userId
                    )
                } else {
                    RetrofitInstance.api.getTotalSalesWithHeaders(
                        accessToken = bearerToken,
                        compCode = compCode,
                        dateFilter = dateFilter.toLowerCase(),
                        userId = userId
                    )
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val totalSales = response.body()!!.TotalSales
                        todaySalesTextView.text = "Total Sales: ₹$totalSales"
                    } else {
                        Log.e("DashboardActivity", "API Error: ${response.code()} - ${response.message()}")
                        todaySalesTextView.text = "Total Sales: Not available"
                    }
                }
            } catch (e: Exception) {
                Log.e("DashboardActivity", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    todaySalesTextView.text = "Total Sales: Error"
                }
            }
        }
    }

    private fun fetchTotalPurchase(dateFilter: String) {
        val compCode = prefManager.getCompanyCode()
        val accessToken = prefManager.getAccessToken()
        val userId = prefManager.getUserId()

        if (compCode.isNullOrEmpty() || accessToken.isNullOrEmpty() || userId.isNullOrEmpty()) {
            todayPurchaseTextView.text = "Missing credentials"
            Log.e("DashboardActivity", "Missing required data from PrefManager")
            return
        }

        val bearerToken = "Bearer $accessToken"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (dateFilter == "Custom" && !startDate.isNullOrEmpty() && !endDate.isNullOrEmpty()) {
                    RetrofitInstance.api.getTotalPurchaseWithHeaders(
                        compCode = compCode,
                        dateFilter = "custom",
                        accessToken = bearerToken,
                        userId = userId
                    )
                } else {
                    RetrofitInstance.api.getTotalPurchaseWithHeaders(
                        compCode = compCode,
                        dateFilter = dateFilter.toLowerCase(),
                        accessToken = bearerToken,
                        userId = userId
                    )
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val totalPurchase = response.body()!!.TotalPurchases
                        todayPurchaseTextView.text = "Total Purchase: ₹$totalPurchase"
                    } else {
                        Log.e("DashboardActivity", "API Error: ${response.code()} - ${response.message()}")
                        todayPurchaseTextView.text = "Total Purchase: Not available"
                    }
                }
            } catch (e: Exception) {
                Log.e("DashboardActivity", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    todayPurchaseTextView.text = "Total Purchase: Error"
                }
            }
        }
    }

}
