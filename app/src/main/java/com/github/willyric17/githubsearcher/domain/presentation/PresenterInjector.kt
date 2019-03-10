package com.github.willyric17.githubsearcher.domain.presentation

import com.github.willyric17.githubsearcher.Injector
import com.github.willyric17.githubsearcher.external.repository.UserRepositoryImpl
import io.reactivex.schedulers.Schedulers

class PresenterInjector : Injector {
    override fun inject(instance: Any) {
        when (instance) {
            is SearchPresenter -> inject(instance)
        }
    }

    companion object {
        lateinit var injector: Injector
    }

    fun inject(presenter: SearchPresenter) {
        with(presenter) {
            scheduler = Schedulers.io()
            userRepository = UserRepositoryImpl()
        }
    }
}