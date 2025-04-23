package com.example.devisoft.models

data class LoginResponse(
    val accessToken: String,
    val data: UserData // `data` is a nested object
)

data class UserData(
    val id: String,
    val companyName: String
    // Add more fields if your API returns them
)
