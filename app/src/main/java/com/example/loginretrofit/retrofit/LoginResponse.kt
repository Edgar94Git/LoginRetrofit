package com.example.loginretrofit.retrofit

data class LoginResponse(var token: String) : SuccessResponse(token)
