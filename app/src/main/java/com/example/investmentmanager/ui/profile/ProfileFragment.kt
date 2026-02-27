package com.example.investmentmanager.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.investmentmanager.LoginActivity
import com.example.investmentmanager.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // 1. Get the SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("userEmail", "user@example.com")

        // 2. Display the email
        binding.emailTextView.text = email

        // 3. Set Logout logic
        binding.logoutButton.setOnClickListener {
            // 1. Clear the login flag from SharedPreferences
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", false)
                remove("userEmail") // Also remove the email
                apply()
            }

            // 2. Go back to LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            // Clear the activity stack
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // 3. Finish the current (MainActivity)
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}