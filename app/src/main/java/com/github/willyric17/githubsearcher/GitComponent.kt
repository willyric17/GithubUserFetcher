package com.github.willyric17.githubsearcher

import com.github.willyric17.githubsearcher.user.UserActivity
import com.github.willyric17.githubsearcher.user.UserModule
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Scope

@GitScope
@Component(
    modules = [
        GitModule::class,
        UserModule::class
    ]
)
interface GitComponent {

    fun inject(activity: UserActivity)

    companion object {
        val component: GitComponent by lazy {
            DaggerGitComponent.builder()
                .build()
        }

        fun inject(injector: (GitComponent) -> Unit) {
            injector(component)
        }
    }
}

@Scope
annotation class GitScope

@Module
class GitModule {

    @Provides
    fun interceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun okhttpClient(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @GitScope
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

}
