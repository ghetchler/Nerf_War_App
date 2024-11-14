package com.cs407.nerf_war_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class create_user_page : AppCompatActivity() {
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_user_page)

        userRepository = UserRepository(this)

        // Get references to UI elements
        val fullName = findViewById<EditText>(R.id.full_name)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password)
        val createAccountButton = findViewById<Button>(R.id.create_account_button)

        // Set button click listener
        createAccountButton.setOnClickListener {
            val nameText = fullName.text.toString()
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            if (nameText.isBlank() || usernameText.isBlank() || passwordText.isBlank() || confirmPasswordText.isBlank()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText == confirmPasswordText) {
                // Perform account creation logic
                val isCreated = userRepository.createUser(nameText, usernameText, passwordText)
                if (isCreated) {
                    Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                    // Go back to User Sign In page
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)  // Start the new activity
                } else {
                    Toast.makeText(this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
