package com.github.willyric17.githubsearcher.user

import com.github.willyric17.githubsearcher.user.repository.UserRepository
import com.github.willyric17.githubsearcher.user.repository.UserRepositoryImpl
import com.github.willyric17.githubsearcher.user.repository.UserService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

@Module
class UserModule {
    @Provides
    fun service(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    fun repository(service: UserService): UserRepository =
        UserRepositoryImpl(service, Schedulers.io())

    @Provides
    fun viewmodel(repository: UserRepository): UserViewModel.Factory =
        UserViewModel.Factory(repository, Schedulers.computation())
}
