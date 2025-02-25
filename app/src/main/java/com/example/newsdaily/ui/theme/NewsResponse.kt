package com.example.newsdaily.ui.theme


data class NewsResponse(
        val status: String,
        val totalResult : Int,
        val articles : List<New>
    )
