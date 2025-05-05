package com.example.devisoft.activities

import DashboardAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.models.DashboardItem
import com.example.devisoft.network.RetrofitInstance
import com.example.devisoft.utils.PrefManager
import kotlinx.coroutines.launch

class TodayPurchaseActivity : AppCompatActivity() {

    private lateinit var purchaseRecyclerView: RecyclerView
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todays_purchase)

        prefManager = PrefManager(this)
        purchaseRecyclerView = findViewById(R.id.purchaseRecyclerView)
        purchaseRecyclerView.layoutManager = LinearLayoutManager(this)


        // Set up the app bar title
        val appBarTitle: TextView = findViewById(R.id.appBarTitle)
        appBarTitle.text = "Todays Purchase"

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

        // Back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        fetchTodayPurchase()
    }

    private fun fetchTodayPurchase() {
        val userId = prefManager.getUserId() ?: ""
        val accessToken = prefManager.getAccessToken() ?: ""
        val compCode = prefManager.getCompanyCode() ?: ""

        if (userId.isBlank() || accessToken.isBlank() || compCode.isBlank()) {
            Log.e("TodayPurchaseActivity", "Missing auth data")
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getPurchaseSummary(compCode, userId, accessToken)
                val purchaseMap = response.associateBy({ it.PAYMENT.uppercase() }, { it.Amount })

                val purchaseItems = listOf(
                    DashboardItem(R.drawable.local, "Local", formatAmount(purchaseMap["LOCAL"])),
                    DashboardItem(R.drawable.outside, "Outside", formatAmount(purchaseMap["OUTSIDE"])),
                    DashboardItem(R.drawable.wholesale, "Wholesale", formatAmount(purchaseMap["WHOLESALE"])),
                    DashboardItem(R.drawable.tax_free, "Tax Free", formatAmount(purchaseMap["TAX FREE"])),
                    DashboardItem(R.drawable.job_work, "Job Work", formatAmount(purchaseMap["JOB WORK"])),
                    DashboardItem(R.drawable.branch, "Branch Transfer", formatAmount(purchaseMap["BRANCH TRANSFER"])),
                    DashboardItem(R.drawable.total_purchase, "Total Purchase", formatAmount(purchaseMap.values.sumOrNull()))
                )

                purchaseRecyclerView.adapter = DashboardAdapter(purchaseItems)

            } catch (e: Exception) {
                Log.e("TodayPurchaseActivity", "API error: ${e.message}")
            }
        }
    }

    private fun formatAmount(amount: Double?): String {
        return when {
            amount == null -> "₹0"
            amount < 0 -> Html.fromHtml("<font color='#FF0000'>₹$amount</font>", Html.FROM_HTML_MODE_LEGACY).toString()
            else -> Html.fromHtml("<font color='#00AA00'>₹$amount</font>", Html.FROM_HTML_MODE_LEGACY).toString()
        }
    }

    private fun Collection<Double?>.sumOrNull(): Double = this.filterNotNull().sum()
}
