package n.com.myapplication.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import n.com.myapplication.screen.user.UserFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

  @ContributesAndroidInjector
  abstract fun contributeUserFragment(): UserFragment

}
