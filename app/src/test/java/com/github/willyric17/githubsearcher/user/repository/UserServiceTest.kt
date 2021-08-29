package com.github.willyric17.githubsearcher.user.repository

import com.github.willyric17.githubsearcher.GitModule
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Ignore("for testing purposes")
class UserServiceTest {
    private lateinit var service: UserService

    @Before
    fun setup() {
        val module = GitModule()
        service = module.interceptor()
            .let { module.okhttpClient(it) }
            .let { module.retrofit(it) }
            .create(UserService::class.java)
    }

    @Test
    fun `test`() {
        service.searchUser("willyric17", 1)
            .observeOn(Schedulers.trampoline())
            .subscribe { response ->
                println(response)
            }
    }
}
