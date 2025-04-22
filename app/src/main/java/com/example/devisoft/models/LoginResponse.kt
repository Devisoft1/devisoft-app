package com.example.devisoft.models

data class LoginResponse(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val data: UserData
)

data class UserData(
    val id: String,
    val email: String,
    val companyName: String
)
