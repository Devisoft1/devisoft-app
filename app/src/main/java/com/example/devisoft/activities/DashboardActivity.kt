package com.example.devisoft.activities

import DashboardAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.models.DashboardItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Set up AppBar title
        val appBarTitle: TextView = findViewById(R.id.appBarTitle)
        appBarTitle.text = "Dashboard"

        val logoImage: ImageView = findViewById(R.id.appBarLogo)
        logoImage.setOnClickListener {
            val url = "https://devisoft.co.in/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Set up the RecyclerViews for sale and purchase
        val saleRecyclerView: RecyclerView = findViewById(R.id.saleRecyclerView)
        val purchaseRecyclerView: RecyclerView = findViewById(R.id.purchaseRecyclerView)

        // Sample data for Sale and Purchase items
        val saleItems = listOf(
            DashboardItem(R.drawable.cash, "Cash", "₹100"),
            DashboardItem(R.drawable.credit, "Credit", "₹200"),
            DashboardItem(R.drawable.card, "Card", "₹150"),
            DashboardItem(R.drawable.wallet, "Wallet", "₹80"),
            DashboardItem(R.drawable.credit_note, "Credit Note", "₹60"),
            DashboardItem(R.drawable.total_sales, "Total Sales", "₹90")
        )

        val purchaseItems = listOf(
            DashboardItem(R.drawable.local, "Local", "₹70"),
            DashboardItem(R.drawable.outside, "Outside", "₹120"),
            DashboardItem(R.drawable.wholesale, "Wholesale", "₹50"),
            DashboardItem(R.drawable.tax_free, "Tax Free", "₹110"),
            DashboardItem(R.drawable.job_work, "Job Work", "₹65"),
            DashboardItem(R.drawable.branch, "Branch Transfer", "₹95"),
            DashboardItem(R.drawable.total_purchase, "Total Purchase ", "₹70"),
        )

        // Set up LinearLayoutManager for vertical scrolling
        val saleLayoutManager = LinearLayoutManager(this)
        val purchaseLayoutManager = LinearLayoutManager(this)

        saleRecyclerView.layoutManager = saleLayoutManager
        purchaseRecyclerView.layoutManager = purchaseLayoutManager

        // Set the adapters for sale and purchase data
        saleRecyclerView.adapter = DashboardAdapter(saleItems)
        purchaseRecyclerView.adapter = DashboardAdapter(purchaseItems)

        // Log the item sizes for debugging
        Log.d("DashboardActivity", "Sale Items Size: ${saleItems.size}")
        Log.d("DashboardActivity", "Purchase Items Size: ${purchaseItems.size}")

        // Set up BottomNavigationView click listener
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_outstanding -> {
                    openOutstandingActivity()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_stocks -> {
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_settings -> {
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    // Function to open OutstandingActivity
    private fun openOutstandingActivity() {
        val intent = Intent(this, OutstandingActivity::class.java)
        startActivity(intent)
    }
}