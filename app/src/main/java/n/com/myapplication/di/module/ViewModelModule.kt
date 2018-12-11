package n.com.myapplication.di.module


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import n.com.myapplication.di.AppViewModelFactory
import n.com.myapplication.di.ViewModelKey
import n.com.myapplication.screen.main.MainActivityViewModel
import n.com.myapplication.screen.user.UserViewModel
import n.com.myapplication.screen.userFavorite.UserFavoriteViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserFavoriteViewModel::class)
    abstract fun bindUserFavoriteViewModel(userFavoriteViewModel: UserFavoriteViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
