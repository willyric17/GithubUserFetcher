package com.github.willyric17.githubsearcher.external.repository

import com.github.willyric17.githubsearcher.Injector
import com.github.willyric17.githubsearcher.external.gateway.NetworkGatewayImpl

class RepositoryInjector : Injector {
    override fun inject(instance: Any) {
        when (instance) {
            is UserRepositoryImpl -> inject(instance)
        }
    }

    companion object {
        lateinit var injector: Injector
    }

    fun inject(userRepository: UserRepositoryImpl) {
        with(userRepository) {
            gateway = NetworkGatewayImpl()
        }
    }
}