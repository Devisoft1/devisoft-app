package com.example.devisoft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.models.OutstandingItem
import com.example.devisoft.R

class OutstandingReceivableAdapter(private val items: List<OutstandingItem>) :
    RecyclerView.Adapter<OutstandingReceivableAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvField1: TextView = view.findViewById(R.id.tvField1)
        val tvField2: TextView = view.findViewById(R.id.tvField2)
        val tvField3: TextView = view.findViewById(R.id.tvField3)
        val tvField4: TextView = view.findViewById(R.id.tvField4)
        val tvField5: TextView = view.findViewById(R.id.tvField5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_outstanding_receivable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvField1.text = item.field1
        holder.tvField2.text = item.field2
        holder.tvField3.text = item.field3
        holder.tvField4.text = item.field4
        holder.tvField5.text = item.field5

        // ✅ Accessibility content description
        holder.itemView.findViewById<LinearLayout>(R.id.rowContainer).contentDescription =
            "Row with field1: ${item.field1}, field2: ${item.field2}, customer: ${item.field5}"
    }

    override fun getItemCount() = items.size
}