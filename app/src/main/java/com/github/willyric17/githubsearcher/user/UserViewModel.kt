package com.github.willyric17.githubsearcher.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.willyric17.githubsearcher.user.repository.UserRepository
import io.reactivex.Scheduler

class UserViewModel(
    private val repository: UserRepository,
    private val scheduler: Scheduler
) : ViewModel() {
    class Factory(
        private val repository: UserRepository,
        private val scheduler: Scheduler
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(repository, scheduler) as T
        }
    }
}
