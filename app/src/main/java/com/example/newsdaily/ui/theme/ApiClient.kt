package com.example.newsdaily.ui.theme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
   private const val BASE_URL = "https://gnews.io/api/v4/"
   // private const val BASE_URL = "https://content.guardianapis.com/"
    //private const val BASE_URL = "https://api.currentsapi.services/v1/"

    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}