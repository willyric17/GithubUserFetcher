# Tech used

- RxJava
- Retrofit
- Dagger Dependency Injection
- Android Lifecycle ViewModel

# Code Structure

The Project is monolithic module, it only contains single module, which is app module as the Android
Application Module

The Application package is `com.github.willyric17.githubsearcher`
package is structured with feature and usage with template
of `com.github.willyric17.githubsearcher.<feature_name>`, in example:

- feature **Splash Screen** placed in `com.github.willyric17.githubsearcher.splash`
- feature **User Search** placed in `com.github.willyric17.githubsearcher.user`

# Test

Unit test is covered in certain class, such as `UserRepositoryImplTest`, in the working
process `UserServiceTest` created to test the configuration of **Retrofit** is correct and can be
use. rather than testing in Android Device, it's faster to test in Unit Testing
