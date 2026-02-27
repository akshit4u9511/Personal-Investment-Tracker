package com.example.investmentmanager.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.investmentmanager.databinding.FragmentHistoryBinding
// We re-use the adapter and viewmodel from the 'investments' package
import com.example.investmentmanager.ui.investments.InvestmentAdapter
import com.example.investmentmanager.ui.investments.InvestmentsViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    // Get the SAME ViewModel as the InvestmentsFragment
    private val investmentsViewModel: InvestmentsViewModel by viewModels()
    private lateinit var adapter: InvestmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // 1. Create the adapter. Clicks won't do anything in this tab.
        adapter = InvestmentAdapter { investment ->
            // We can leave this empty for the history tab
            // Or, you could show a read-only detail dialog
        }

        // 2. Setup the RecyclerView
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 3. Observe the data
        investmentsViewModel.allArchivedInvestments.observe(viewLifecycleOwner) { investments ->
            investments?.let {
                adapter.submitList(it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
