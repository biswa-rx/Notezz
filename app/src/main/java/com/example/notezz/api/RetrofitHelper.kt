package com.example.notezz.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
//    private const val BASE_URL = "http://192.168.64.137:3000/"
    private const val BASE_URL = "http://192.168.1.37:3000/"  //for my router setup
    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}