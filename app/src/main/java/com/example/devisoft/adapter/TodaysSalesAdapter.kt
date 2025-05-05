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

class TodaySalesAdapter(private val items: List<DashboardItem>) :
    RecyclerView.Adapter<TodaySalesAdapter.SalesViewHolder>() {

    inner class SalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.cardIcon)
        val title: TextView = itemView.findViewById(R.id.cardTitle)
        val amount: TextView = itemView.findViewById(R.id.cardAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return SalesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId)
        holder.title.text = item.title
        holder.amount.text = Html.fromHtml(item.amount, Html.FROM_HTML_MODE_COMPACT)
    }

    override fun getItemCount() = items.size
}
