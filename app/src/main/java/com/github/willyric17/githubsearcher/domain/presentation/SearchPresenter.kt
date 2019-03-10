package com.github.willyric17.githubsearcher.domain.presentation

import com.github.willyric17.githubsearcher.external.repository.UserRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

class SearchPresenter : SearchContract.Presenter {
    private lateinit var view: SearchContract.View
    lateinit var scheduler: Scheduler
    lateinit var userRepository: UserRepository

    init {
        PresenterInjector.injector.inject(this)
    }

    private var page = 1
    private lateinit var keyword: String
    private val disposables = CompositeDisposable()
    private var end = false

    override fun search(query: String) {
        keyword = query
        end = false
        page = 0
        view.clearResult()
        nextPage()
    }

    override fun nextPage() {
        if (end) return
        userRepository.searchUser(keyword, ++page)
            .subscribeOn(scheduler)
            .subscribe({
                if (it.size == 0) {
                    end = true
                    view.endOfResult()
                } else {
                    view.appendResult(it)
                }
            }, {
                when (it) {
                    is HttpException -> {
                        view.onError(it.code())
                    }
                }
            }).also { disposables.add(it) }
    }

    override fun attachView(view: SearchContract.View) {
        this.view = view
    }

    override fun destroy() {
        disposables.clear()
    }
}