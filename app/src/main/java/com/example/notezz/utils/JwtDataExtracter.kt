package com.example.notezz.utils

import com.auth0.android.jwt.JWT


object JwtDataExtracter {
    fun getName(jwtToken: String):String {
        try{
            val jwt = JWT(jwtToken)
            val claim = jwt.getClaim("name").asString()
            return claim.toString()
        } catch (e: Exception) {
         println(e.message)
        }
        return "null"
    }
    fun getIssuer(jwtToken: String):String {
        try {
            val jwt = JWT(jwtToken)
            val claim = jwt.issuer
            return claim.toString()
        } catch (e:Exception){
            println(e.message)
        }
        return "null"
    }
    fun isExpire(jwtToken: String): Boolean {
        try {
            val jwt = JWT(jwtToken)
            return jwt.isExpired(10)
        } catch (e:Exception){
            println(e.message)
        }
        return true
    }
    fun getAudience(jwtToken: String): String {
        try {
            val jwt = JWT(jwtToken)
            return jwt.audience.toString()
        } catch (e:Exception){
            println(e.message)
        }
        return "null"
    }
}