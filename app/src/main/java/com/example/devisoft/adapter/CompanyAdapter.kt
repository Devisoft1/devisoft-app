package com.example.devisoft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.models.Company


class CompanyAdapter(
    private val companies: List<Company>,
    private val onItemClick: (Company) -> Unit // click listener passed from activity
) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return CompanyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(companies[position])
    }

    override fun getItemCount(): Int = companies.size

    inner class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.companyNameTextView)

        fun bind(company: Company) {
            nameTextView.text = company.Name

            itemView.setOnClickListener {
                onItemClick(company) // trigger callback on click
            }
        }
    }
}
