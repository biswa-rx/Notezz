package com.example.notezz.utils

object AccessTokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    fun setAccessToken(token: String) {
        accessToken = token
    }

    fun getAccessToken(): String? {
        return accessToken
    }

    fun setRefreshToken(token: String) {
        refreshToken = token
    }

    fun getRefreshToken(): String? {
        return refreshToken
    }
}
