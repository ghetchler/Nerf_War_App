package com.cs407.nerf_war_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepository = UserRepository(this)

        // Find the Sign In button and set a click listener to navigate to welcome page
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        signInButton.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            if (usernameText.isBlank() || passwordText.isBlank()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userRepository.validateUser(usernameText, passwordText)) {
                // Sign in success
                val intent = Intent(this, home_page::class.java)
                startActivity(intent)  // Start the new activity
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Find the Create User button and set a click listener to navigate to create user page
        val newUserButton = findViewById<Button>(R.id.create_user_button)
        newUserButton.setOnClickListener {
            val intent = Intent(this, create_user_page::class.java)
            startActivity(intent)  // Start the new activity
        }
    }
}
