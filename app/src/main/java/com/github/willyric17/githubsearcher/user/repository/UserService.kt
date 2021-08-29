package com.github.willyric17.githubsearcher.user.repository

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("search/users")
    fun searchUser(@Query("q") keyword: String, @Query("page") page: Int): Single<SearchResponse>
}

data class SearchResponse(
    @SerializedName("total_count") val total: Int,
    @SerializedName("incomplete_results") val incomplete: Boolean,
    @SerializedName("items") val users: List<UserResponse>
)

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val username: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("avatar_url") val avatar: String
)
