package com.github.willyric17.githubsearcher.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(url: String, view: ImageView) {
    Glide.with(view).load(url).into(view)
}