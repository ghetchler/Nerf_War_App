package com.cs407.nerf_war_app

import android.content.Context
import android.content.SharedPreferences

class UserRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserRepository", Context.MODE_PRIVATE)

    fun createUser(fullName: String, username: String, password: String): Boolean {
        if (sharedPreferences.contains(username)) {
            // User already exists
            return false
        }
        val editor = sharedPreferences.edit()
        editor.putString("$username-fullName", fullName)
        editor.putString("$username-password", password)
        editor.apply()
        return true
    }

    fun validateUser(username: String, password: String): Boolean {
        val storedPassword = sharedPreferences.getString("$username-password", null)
        return storedPassword == password
    }
}
