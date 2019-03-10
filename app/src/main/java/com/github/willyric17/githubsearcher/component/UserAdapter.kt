package com.github.willyric17.githubsearcher.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.willyric17.githubsearcher.R
import com.github.willyric17.githubsearcher.domain.entity.GithubUser
import com.github.willyric17.githubsearcher.util.loadImage
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(val users: ArrayList<GithubUser>) : RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(users[position]) {
            holder.label.text = name
            loadImage(avatar,holder.image)
        }
    }
}

class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val label by lazy { view.userName }
    val image by lazy { view.userAvatar }
}