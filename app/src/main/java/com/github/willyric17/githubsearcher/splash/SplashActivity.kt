package com.github.willyric17.githubsearcher.splash

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.willyric17.githubsearcher.R
import com.github.willyric17.githubsearcher.databinding.ActivitySplashBinding
import com.github.willyric17.githubsearcher.load
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

    }

    lateinit var splashTimer: Disposable

    override fun onStart() {
        super.onStart()
        splashTimer = Completable.timer(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                binding.image.apply {
                    load(R.mipmap.octocat)
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
                }
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show()
            }
    }
}
