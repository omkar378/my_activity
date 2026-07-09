package com.example.mytaskflow

object UserManager {
    var userName: String = "Guest"
    var userEmail: String = "guest@example.com"

    fun setUser(name: String, email: String) {
        userName = name
        userEmail = email
    }
}
