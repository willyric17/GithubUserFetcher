package com.github.willyric17.githubsearcher.user.repository

import io.reactivex.Single

interface UserRepository {
    fun getUser(keyword: String, page: Int): Single<List<UserEntity>>
}
