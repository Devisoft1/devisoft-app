package com.example.devisoft.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devisoft.R
import com.example.devisoft.models.Company
import com.example.devisoft.activities.CompanyDetailsActivity  // Import the relevant activity

class CompanyAdapter(private val companies: List<Company>) :
    RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return CompanyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companies[position]
        holder.bind(company)
    }

    override fun getItemCount(): Int = companies.size

    inner class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.companyNameTextView)

        fun bind(company: Company) {
            nameTextView.text = company.Name

            itemView.setOnClickListener {
                // When the company is clicked, pass its code in the URL (intent)
                val context = itemView.context
                val intent = Intent(context, CompanyDetailsActivity::class.java)
                // You can add the company code or other details in the Intent
                intent.putExtra("COMPANY_CODE", company.CompCode)
                context.startActivity(intent)
            }
        }
    }
}
