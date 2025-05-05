package com.example.devisoft.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.models.DashboardItem

class DashboardAdapter(private val items: List<DashboardItem>) :
    RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    // ViewHolder class that binds views for each item
    inner class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.cardIcon) // assuming cardIcon is the ImageView ID
        val title: TextView = itemView.findViewById(R.id.cardTitle) // assuming cardTitle is the TextView ID
        val amount: TextView = itemView.findViewById(R.id.cardAmount) // assuming cardAmount is the TextView ID
    }

    // Create a new view (called by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false) // Assuming item_dashboard is your item layout XML
        return DashboardViewHolder(view)
    }

    // Replace the contents of a view (called by the layout manager)
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId) // Set the icon image
        holder.title.text = item.title // Set the title text
        holder.amount.text = Html.fromHtml(item.amount, Html.FROM_HTML_MODE_COMPACT) // Set the amount with HTML formatting
    }

    // Return the total number of items
    override fun getItemCount() = items.size
}
