package com.github.willyric17.githubsearcher.external.gateway

import com.github.willyric17.githubsearcher.external.gateway.retrofit.GithubServiceV3
import com.github.willyric17.githubsearcher.external.response.UserSearchResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


interface NetworkGateway {
    fun searchUser(query: String, page: Int): Single<UserSearchResponse>
}

class NetworkGatewayImpl : NetworkGateway {
    companion object {
        val intercetor by lazy {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
        val okhttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(intercetor)
                .build()
        }

        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okhttpClient)
                .build()
        }
    }

    val githubService: GithubServiceV3 by lazy {
        retrofit.create(GithubServiceV3::class.java)
    }

    override fun searchUser(query: String, page: Int): Single<UserSearchResponse> =
        githubService.searchUser(query, page)
}