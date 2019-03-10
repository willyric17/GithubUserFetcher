package com.github.willyric17.githubsearcher.external.repository

import com.github.willyric17.githubsearcher.Injector
import com.github.willyric17.githubsearcher.domain.entity.GithubUser
import com.github.willyric17.githubsearcher.external.gateway.NetworkGateway
import com.github.willyric17.githubsearcher.external.response.User
import com.github.willyric17.githubsearcher.external.response.UserSearchResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    val injector = object : Injector {
        override fun inject(instance: Any) {
            when (instance) {
                is UserRepositoryImpl ->
                    instance.gateway = networkGateway
            }
        }
    }

    lateinit var networkGateway: NetworkGateway
    @Before
    fun setUp() {
        RepositoryInjector.injector = injector
        networkGateway = mock()
    }

    @Test
    fun `searchUser converts Response to Entity`() {
        val users = listOf(
            User(1, "pokemon_1", "User", "url", "avatar"),
            User(2, "pokemon_2", "User", "url", null),
            User(3, "pokemon_3", "User", "url", "avatar"),
            User(4, "pokemon_4", "User", "url", "avatar")
        )
        val reponse = UserSearchResponse(1, false, users)
        networkGateway.stub {
            on { searchUser(any(), any()) }.thenReturn(Single.just(reponse))
        }

        val repository = UserRepositoryImpl()
        repository.searchUser("pokemon", 0).test()
            .assertValue { value ->
                value.containsAll(
                    listOf(
                        GithubUser(1, "pokemon_1", "avatar"),
                        GithubUser(2, "pokemon_2", ""),
                        GithubUser(3, "pokemon_3", "avatar"),
                        GithubUser(4, "pokemon_4", "avatar")
                    )
                )
            }
    }
}