package com.example.newsdaily.ui.theme

data class NewsJSON(
val page: Int,
val totalResults: String,
val articles: List<New>
)
