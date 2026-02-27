package com.example.investmentmanager.ui.investments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.investmentmanager.database.Investment
// These imports are for View Binding
import com.example.investmentmanager.databinding.DialogAddInvestmentBinding
import com.example.investmentmanager.databinding.FragmentInvestmentsBinding

class InvestmentsFragment : Fragment() {

    private var _binding: FragmentInvestmentsBinding? = null
    private val binding get() = _binding!!

    // Get the ViewModel using the 'by viewModels' delegate
    private val investmentsViewModel: InvestmentsViewModel by viewModels()

    private lateinit var adapter: InvestmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvestmentsBinding.inflate(inflater, container, false)

        // 1. Create the adapter, passing it our click handler function
        adapter = InvestmentAdapter { investment ->
            // Handle click: show dialog to edit/delete
            showOptionsDialog(investment)
        }

        // 2. Setup the RecyclerView
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 3. Observe the data from the ViewModel (for ACTIVE investments)
        investmentsViewModel.allActiveInvestments.observe(viewLifecycleOwner) { investments ->
            // When data changes, submit the new list to the adapter
            investments?.let {
                adapter.submitList(it)
            }
        }

        return binding.root
    }

    // A single dialog for BOTH adding and editing
    fun showAddEditDialog(investment: Investment?) {
        // Inflate the dialog layout
        val dialogBinding = DialogAddInvestmentBinding.inflate(layoutInflater)
        val isEditMode = (investment != null)

        // If Edit Mode, pre-fill the fields
        if (isEditMode) {
            dialogBinding.etName.setText(investment!!.name)
            dialogBinding.etBroker.setText(investment.broker)
            dialogBinding.etAmount.setText(investment.amount.toString())
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (isEditMode) "Edit Investment" else "Add Investment")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val name = dialogBinding.etName.text.toString().trim()
                val broker = dialogBinding.etBroker.text.toString().trim()
                val amount = dialogBinding.etAmount.text.toString().toDoubleOrNull()

                if (name.isBlank() || broker.isBlank() || amount == null) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton // Stop execution
                }

                val newOrUpdatedInvestment = Investment(
                    id = investment?.id ?: 0, // Use old ID if editing, 0 for new
                    name = name,
                    broker = broker,
                    amount = amount,
                    date = investment?.date ?: System.currentTimeMillis(), // Use old date or current time
                    isActive = true // New or edited investments are always active
                )

                if (isEditMode) {
                    investmentsViewModel.update(newOrUpdatedInvestment)
                } else {
                    investmentsViewModel.insert(newOrUpdatedInvestment)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Dialog that shows "Edit" and "Delete" options
    private fun showOptionsDialog(investment: Investment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Options")
            // We call our "Delete" an "Archive" in the code
            .setItems(arrayOf("Edit", "Delete (Archive)")) { dialog, which ->
                when (which) {
                    0 -> showAddEditDialog(investment) // Edit
                    1 -> showDeleteConfirmation(investment) // Archive
                }
            }
            .show()
    }

    // Final confirmation before "deleting" (archiving)
    private fun showDeleteConfirmation(investment: Investment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Investment")
            .setMessage("Are you sure you want to delete '${investment.name}'? This will move it to History.")
            .setPositiveButton("Delete") { _, _ ->
                // Call the ARCHIVE function, not delete
                investmentsViewModel.archive(investment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // This is important to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}