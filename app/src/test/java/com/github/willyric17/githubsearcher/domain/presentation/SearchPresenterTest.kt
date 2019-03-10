package com.github.willyric17.githubsearcher.domain.presentation

import com.github.willyric17.githubsearcher.Injector
import com.github.willyric17.githubsearcher.domain.entity.GithubUser
import com.github.willyric17.githubsearcher.external.repository.UserRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.adapter.rxjava2.HttpException

class SearchPresenterTest {
    companion object {
        private val userList_page1 = listOf(
            GithubUser(1, "user1", "avatar"),
            GithubUser(2, "user2", "avatar"),
            GithubUser(3, "user3", "avatar")
        )
        private val userList_page2 = listOf(
            GithubUser(4, "user4", "avatar"),
            GithubUser(5, "user5", "avatar"),
            GithubUser(6, "user6", "avatar")
        )
        private val response403 =
            Response.error<String>(403, ResponseBody.create(MediaType.parse("application;json"), ""))
    }

    lateinit var repository: UserRepository
    lateinit var view: SearchContract.View
    val injector = object : Injector {
        override fun inject(instance: Any) {
            when (instance) {
                is SearchPresenter -> {
                    instance.userRepository = repository
                    instance.scheduler = Schedulers.trampoline()
                }
            }
        }
    }

    @Before
    fun setUp() {
        repository = mock()
        view = mock()
        PresenterInjector.injector = injector
    }

    @Test
    fun `searchQuery will removeItems and appendNewResult, nextpage will appendNewPage`() {
        repository.stub {
            on { searchUser(any(), eq(1)) }.thenReturn(Single.just(userList_page1))
            on { searchUser(any(), eq(2)) }.thenReturn(Single.just(userList_page2))
        }
        val presenter = SearchPresenter()
        presenter.attachView(view)
        presenter.search("")
        presenter.nextPage()

        verify(view, times(1)).clearResult()
        verify(view, times(1)).appendResult(userList_page1)
        verify(view, times(1)).appendResult(userList_page2)
    }

    @Test
    fun `searchQuery will reset query and page`() {
        repository.stub {
            on { searchUser(eq("1"), any()) }.thenReturn(Single.just(userList_page1))
            on { searchUser(eq("2"), any()) }.thenReturn(Single.just(userList_page2))
        }
        val presenter = SearchPresenter()
        presenter.attachView(view)
        presenter.search("1")
        presenter.search("2")

        verify(view, times(2)).clearResult()
        verify(view, times(1)).appendResult(userList_page1)
        verify(view, times(1)).appendResult(userList_page2)
    }

    @Test
    fun `nextPage should call limit`() {
        repository.stub {
            on { searchUser(any(), eq(1)) }.thenReturn(Single.just(userList_page1))
            on { searchUser(any(), eq(2)) }.thenReturn(Single.error(HttpException(response403)))
        }
        val presenter = SearchPresenter()
        presenter.attachView(view)
        presenter.search("")
        presenter.nextPage()

        verify(view, times(1)).clearResult()
        verify(view, times(1)).appendResult(userList_page1)
        verify(view, times(1)).onError(403)
    }

    @Test
    fun `when repository returns zero list, will call endResult and nextPage have no execution`() {
        repository.stub {
            on { searchUser(any(), eq(1)) }.thenReturn(Single.just(userList_page1))
            on { searchUser(any(), eq(2)) }.thenReturn(Single.just(emptyList()))
        }
        val presenter = SearchPresenter()
        presenter.attachView(view)
        presenter.search("")
        presenter.nextPage()

        verify(view, times(1)).clearResult()
        verify(view, times(1)).appendResult(userList_page1)
        verify(view, times(1)).endOfResult()
        verify(view, never()).appendResult(emptyList())

        presenter.nextPage()
        verify(repository, never()).searchUser(any(), eq(3))
    }

}