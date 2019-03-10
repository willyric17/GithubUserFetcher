package com.github.willyric17.githubsearcher.external.repository

import com.github.willyric17.githubsearcher.domain.entity.GithubUser
import com.github.willyric17.githubsearcher.external.gateway.NetworkGateway
import io.reactivex.Single

interface UserRepository {
    fun searchUser(query: String, page: Int): Single<List<GithubUser>>
}

class UserRepositoryImpl : UserRepository {
    lateinit var gateway: NetworkGateway

    init {
        RepositoryInjector.injector.inject(this)
    }

    override fun searchUser(query: String, page: Int): Single<List<GithubUser>> {
        return gateway.searchUser(query, page)
            .map { response ->
                response.users.map { user ->
                    GithubUser(user.id, user.login, user.avatarUrl ?: "")
                }
            }
    }
}

