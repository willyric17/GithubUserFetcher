package com.github.willyric17.githubsearcher

import com.github.willyric17.githubsearcher.domain.presentation.SearchPresenter

class ActivityInjector : Injector {
    override fun inject(instance: Any) {
        when (instance) {
            is SearchActivity -> inject(instance)
        }
    }

    companion object {
        lateinit var injector: Injector
    }

    fun inject(activity: SearchActivity) {
        activity.searchPresenter = SearchPresenter()
    }
}