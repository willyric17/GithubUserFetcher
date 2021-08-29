package com.github.willyric17.githubsearcher.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.willyric17.githubsearcher.GitComponent
import com.github.willyric17.githubsearcher.databinding.ActivityUserBinding
import com.github.willyric17.githubsearcher.databinding.ItemUserBinding
import com.github.willyric17.githubsearcher.load
import com.github.willyric17.githubsearcher.user.repository.UserEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserActivity:AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: UserViewModel.Factory

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(UserViewModel::class.java)
    }

    lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GitComponent.inject { it.inject(this) }

        observe()
    }

    val disposables = CompositeDisposable()

    private val adapter = UserAdapter()

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    private fun observe() {
        Observable.create<String> { source ->
            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    source.onNext(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnDispose { binding.search.setOnQueryTextListener(null) }
            .subscribe { viewModel.setKeyword(it) }
            .also { disposables.add(it) }

        binding.resultList.adapter = adapter
        binding.resultList.layoutManager = LinearLayoutManager(this)

        viewModel.usersSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        Toast.makeText(this, "No result found", Toast.LENGTH_SHORT).show()
                    } else if (it.size == adapter.itemCount) {
                        Toast.makeText(this, "End of result", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.items.apply {
                            clear()
                            addAll(it)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }, {
                    it.printStackTrace()
                })
            .also { disposables.add(it) }

        Observable.create<Unit> { source ->
            binding.resultList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        source.onNext(Unit)
                    }
                }
            })
        }.subscribe {
            viewModel.nextPage()
        }.also { disposables.add(it) }
    }
}


internal class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var onItemSelected: ((UserEntity) -> Unit)? = null

    val items = ArrayList<UserEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemSelected?.invoke(item)
            }
        }

        lateinit var item: UserEntity

        fun bind(item: UserEntity) {
            this.item = item
            binding.label.text = item.username
            binding.avatar.load(item.avatar)
        }
    }
}
