package n.com.myapplication.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import n.com.myapplication.screen.user.UserFragment
import n.com.myapplication.screen.userFavorite.UserFavoriteFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

  @ContributesAndroidInjector
  abstract fun contributeUserFragment(): UserFragment

  @ContributesAndroidInjector
  abstract fun contributeUserFavoriteFragment(): UserFavoriteFragment

}
