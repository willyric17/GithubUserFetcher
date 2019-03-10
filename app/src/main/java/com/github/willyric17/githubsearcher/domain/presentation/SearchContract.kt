package com.github.willyric17.githubsearcher.domain.presentation

import com.github.willyric17.githubsearcher.domain.entity.GithubUser

interface SearchContract {
    interface Presenter : BasePresenter<View> {
        fun search(query: String)
        fun nextPage()
    }

    interface View {
        fun clearResult()
        fun appendResult(list: List<GithubUser>)
        fun endOfResult()
        fun onError(code: Int)
    }
}