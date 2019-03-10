package com.github.willyric17.githubsearcher

import android.app.Application
import com.github.willyric17.githubsearcher.domain.presentation.PresenterInjector
import com.github.willyric17.githubsearcher.external.repository.RepositoryInjector

class SearchApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ActivityInjector.injector = ActivityInjector()
        PresenterInjector.injector = PresenterInjector()
        RepositoryInjector.injector = RepositoryInjector()
    }
}