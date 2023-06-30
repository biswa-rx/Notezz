package com.example.notezz.model.auth_model

import com.google.gson.annotations.SerializedName

data class AuthorizationResponse(
    @SerializedName("accessToken")
    val ACCESS_TOKEN: String,
    @SerializedName("refreshToken")
    val REFRESH_TOKEN: String,
)

