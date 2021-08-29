package com.github.willyric17.githubsearcher.user.repository

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class UserRepositoryImplTest {
    private lateinit var service: UserService
    private lateinit var impl: UserRepositoryImpl

    @Before
    fun setup() {
        service = mockk()
        impl = UserRepositoryImpl(service, Schedulers.trampoline())
    }

    @Test
    fun `test searchUser should returns data from service and convert it to domain entity`() {
        val keyword = Random.nextString()
        val page = 0
        val response = SearchResponse(
            Random.nextInt(), Random.nextBoolean(), Array(Random.nextInt(3, 100)) {
                UserResponse(
                    Random.nextInt(),
                    Random.nextString(),
                    Random.nextString(),
                    Random.nextString(),
                    Random.nextString()
                )
            }.toList()
        )
        every { service.searchUser(eq(keyword), eq(page)) } returns Single.just(response)

        impl.getUser(keyword, page)
            .test()
            .run {
                assertComplete()
                assertNoErrors()

                val values = values()
                assertEquals(1, values.size)

                val value = values.first()
                assertEquals(response.users.size, value.size)
                repeat(response.users.size) {
                    assertEquals(response.users[it].id, value[it].id)
                }

            }
    }
}

const val CHARACTERS = "qwertyuioplkjhgfdsazxcvbnm, QWERTYUIOPLKJHGFDSAZXCVBNM 1234567890"
const val LENGTH = CHARACTERS.length

fun Random.nextString(): String = nextInt(100).let { size ->
    StringBuilder(size).apply {
        repeat(size) {
            append(CHARACTERS[nextInt(LENGTH)])
        }
    }.toString()
}
