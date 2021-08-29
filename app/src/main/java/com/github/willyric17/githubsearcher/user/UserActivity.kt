package com.github.willyric17.githubsearcher.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.willyric17.githubsearcher.GitComponent
import javax.inject.Inject

class UserActivity:AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: UserViewModel.Factory

    val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GitComponent.inject { it.inject(this) }
    }
}
