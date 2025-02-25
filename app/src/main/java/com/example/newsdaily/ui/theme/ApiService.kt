package com.example.newsdaily.ui.theme


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
     suspend fun getTopHeadlines (
        @Query("token") apiKey: String,
        @Query("lang") language: String = "en",
        @Query("country") country: String = "us"
    ): Response<NewsResponse>

}