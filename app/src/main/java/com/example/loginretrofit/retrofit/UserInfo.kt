package com.example.loginretrofit.retrofit

import com.example.loginretrofit.Constants
import com.google.gson.annotations.SerializedName

class UserInfo (
    @SerializedName(Constants.EMAIL_PARAM) val email: String,
    @SerializedName(Constants.PASSWORD_PARAM) val pass: String
)