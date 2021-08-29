package com.github.willyric17.githubsearcher.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.willyric17.githubsearcher.user.repository.UserEntity
import com.github.willyric17.githubsearcher.user.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class UserViewModel(
    private val repository: UserRepository,
    private val scheduler: Scheduler
) : ViewModel() {

    private val keywordState = PublishSubject.create<String>()

    private val pageState = PublishSubject.create<Int>()

    val usersSubject = BehaviorSubject.create<List<UserEntity>>()

    init {
        Observable.combineLatest(
            keywordState.distinctUntilChanged(),
            pageState.scan { prev, current ->
                if (current == 0) {
                    1
                } else {
                    prev.inc()
                }
            }
        ) { keyword, page -> keyword to page }
            .subscribeOn(scheduler)
            .debounce(50, TimeUnit.MILLISECONDS)
            .flatMapSingle { (keyword, page) ->
                repository.getUser(keyword, page).map { users ->
                    keyword to users
                }
            }
            .observeOn(scheduler)
            .scan { previous, current ->
                if (previous.first == current.first) {
                    previous.first to (previous.second + current.second)
                } else {
                    current
                }
            }.map { it.second }
            .subscribe(usersSubject)
    }

    fun setKeyword(keyword: String) {
        keywordState.onNext(keyword)
        pageState.onNext(0)
    }

    fun nextPage() {
        pageState.onNext(1)
    }

    class Factory(
        private val repository: UserRepository,
        private val scheduler: Scheduler
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(repository, scheduler) as T
        }
    }
}
