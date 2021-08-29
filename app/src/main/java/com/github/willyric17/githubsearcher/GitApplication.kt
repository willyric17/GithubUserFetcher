package com.github.willyric17.githubsearcher

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class GitApplication : MultiDexApplication() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
