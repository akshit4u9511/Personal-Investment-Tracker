package com.example.investmentmanager.ui.investments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentmanager.database.Investment
// Make sure this import matches your item_investment.xml file
import com.example.investmentmanager.databinding.ItemInvestmentBinding
import java.text.SimpleDateFormat
import java.util.*

// We pass in a function from the Fragment to handle clicks
class InvestmentAdapter(private val onClick: (Investment) -> Unit) :
    ListAdapter<Investment, InvestmentAdapter.InvestmentViewHolder>(InvestmentDiffCallback) {

    // This is the "ViewHolder" - it holds the views for ONE row
    class InvestmentViewHolder(private val binding: ItemInvestmentBinding, val onClick: (Investment) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentInvestment: Investment? = null
        // Formatter for the date
        private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        init {
            // Set the click listener for the entire row
            itemView.setOnClickListener {
                currentInvestment?.let {
                    onClick(it)
                }
            }
        }

        // This function binds the data to the views
        fun bind(investment: Investment) {
            currentInvestment = investment
            binding.investmentName.text = investment.name
            binding.investmentBroker.text = investment.broker
            binding.investmentAmount.text = "₹%.2f".format(investment.amount) // Format to 2 decimal places
            binding.investmentDate.text = sdf.format(Date(investment.date))
        }
    }

    // Called when the RecyclerView needs a new row layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestmentViewHolder {
        val binding = ItemInvestmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvestmentViewHolder(binding, onClick)
    }

    // Called when the RecyclerView wants to display data in a row
    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

// This helps the adapter run efficiently by figuring out what changed
object InvestmentDiffCallback : DiffUtil.ItemCallback<Investment>() {
    override fun areItemsTheSame(oldItem: Investment, newItem: Investment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Investment, newItem: Investment): Boolean {
        return oldItem == newItem
    }
}