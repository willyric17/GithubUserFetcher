package com.github.willyric17.githubsearcher.external.gateway.retrofit

import com.github.willyric17.githubsearcher.external.response.UserSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubServiceV3 {
    @GET("search/users")
    fun searchUser(@Query("q") query: String, @Query("page") page: Int): Single<UserSearchResponse>
}