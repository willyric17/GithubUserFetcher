package com.github.willyric17.githubsearcher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.willyric17.githubsearcher.component.UserAdapter
import com.github.willyric17.githubsearcher.domain.entity.GithubUser
import com.github.willyric17.githubsearcher.domain.presentation.SearchContract
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(), SearchContract.View {

    lateinit var searchPresenter: SearchContract.Presenter
    val disposables = CompositeDisposable()
    val users = ArrayList<GithubUser>()
    val userAdapter by lazy { UserAdapter(users) }

    override fun clearResult() {
        users.clear()
        runOnUiThread {
            userAdapter.notifyDataSetChanged()
        }
    }

    override fun onError(code: Int) {
        runOnUiThread {
            when (code) {
                403 -> Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Unknown error occured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun appendResult(list: List<GithubUser>) {
        if (list.size != 0 && !users.contains(list[0])) users.addAll(list)
        runOnUiThread {
            userAdapter.notifyDataSetChanged()
        }
    }

    override fun endOfResult() {
        runOnUiThread {
            Toast.makeText(this, "End of result", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        ActivityInjector.injector.inject(this)
        searchPresenter.attachView(this)

        bindView()
        prepareView()
    }

    private fun prepareView() {
        listResult.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }
    }

    private fun bindView() {
        RxTextView.textChangeEvents(inputSearch)
            .filter { it.text().length > 0 }
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(
                { searchPresenter.search(it.text().toString()) }
            ).also { disposables.add(it) }

        RxRecyclerView.scrollEvents(listResult)
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .map {
                val layoutManager = it.view().layoutManager as LinearLayoutManager
                val last = layoutManager.findLastVisibleItemPosition()
                val total = layoutManager.itemCount

                (total - last < 15) to last
            }
            .filter { it.first }
            .map { it.second }
            .throttleFirst(400, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe(
                { searchPresenter.nextPage() }
            ).also { disposables.add(it) }
    }
}