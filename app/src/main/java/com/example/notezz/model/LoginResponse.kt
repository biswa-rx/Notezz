package com.example.notezz.model

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class LoginResponse(
    @SerializedName("accessToken")
    val ACCESS_TOKEN: String,
    @SerializedName("refreshToken")
    val REFRESH_TOKEN: String,
)

