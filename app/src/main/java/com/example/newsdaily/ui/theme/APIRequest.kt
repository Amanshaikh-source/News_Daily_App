package com.example.newsdaily.ui.theme

import retrofit2.http.GET

interface APIRequest {
        @GET("/v2/everything?q=tesla&from=2025-01-15&sortBy=publishedAt&apiKey=f59309b8783b453984582f1edee73a7c")
//    @GET("/v2/top-headlines?country=in&apiKey=f59309b8783b453984582f1edee73a7c")
//    @GET("/v1/latest-news?language=it&apiKey=7goxs3CKQUAJRGhquf6BovIkIToi_vj5Uii2lTrQCVE9EtY2")
    suspend fun getNews() : NewsJSON

}