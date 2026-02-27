
package com.example.investmentmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.investmentmanager.database.User
import com.example.investmentmanager.databinding.ActivityRegisterBinding
import com.example.investmentmanager.ui.UserViewModel
import com.example.investmentmanager.ui.UserViewModelFactory
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = userViewModel.findByEmail(email)
                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "User already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
                    val newUser = User(email = email, passwordHash = passwordHash)
                    userViewModel.insert(newUser)
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
