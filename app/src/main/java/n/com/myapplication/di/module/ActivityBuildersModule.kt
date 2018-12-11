package n.com.myapplication.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import n.com.myapplication.screen.main.MainActivity

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeMainActivity(): MainActivity
}
