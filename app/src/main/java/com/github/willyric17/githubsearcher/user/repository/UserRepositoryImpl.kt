package com.github.willyric17.githubsearcher.user.repository

import io.reactivex.Scheduler
import io.reactivex.Single

class UserRepositoryImpl(
    private val service: UserService,
    private val scheduler: Scheduler
) : UserRepository {
    override fun getUser(keyword: String, page: Int): Single<List<UserEntity>> =
        service.searchUser(keyword, page)
            .subscribeOn(scheduler)
            .map {
                it.users.map { user ->
                    UserEntity(
                        user.id,
                        user.username,
                        user.avatar
                    )
                }
            }
}
