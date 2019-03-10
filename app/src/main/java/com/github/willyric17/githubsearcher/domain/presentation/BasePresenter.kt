package com.github.willyric17.githubsearcher.domain.presentation

interface BasePresenter<T> {
    fun attachView(view: T)
    fun destroy()
}