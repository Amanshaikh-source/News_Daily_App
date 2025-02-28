package com.example.newsdaily.ui.theme

import com.google.gson.annotations.SerializedName

data class New(
    val source: Source?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("image")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?
)

data class Source(
    val id: String?,
    val name: String?
)
