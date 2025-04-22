package com.example.devisoft.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.models.FinancialYear

class FinancialYearAdapter(private val financialYears: List<FinancialYear>) :
    RecyclerView.Adapter<FinancialYearAdapter.FinancialYearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancialYearViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_financial_year, parent, false)
        return FinancialYearViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinancialYearViewHolder, position: Int) {
        val financialYear = financialYears[position]
        holder.bind(financialYear)
    }

    override fun getItemCount(): Int = financialYears.size

    class FinancialYearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val yearTextView: TextView = itemView.findViewById(R.id.yearTextView)

        fun bind(financialYear: FinancialYear) {
            yearTextView.text = financialYear.fyear
        }
    }
}
