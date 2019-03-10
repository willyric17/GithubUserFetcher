package com.github.willyric17.githubsearcher.external.response

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("type")
    val type: String? = null,
    @field:SerializedName("url")
    val url: String? = null,
    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null
)