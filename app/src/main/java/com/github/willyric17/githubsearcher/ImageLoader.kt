package com.github.willyric17.githubsearcher

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.load(@DrawableRes resId: Int) {
    Glide.with(this)
        .load(resId)
        .into(this)
}

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}
