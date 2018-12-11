package n.com.myapplication.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import n.com.myapplication.MainApplication
import n.com.myapplication.di.module.ActivityBuildersModule
import n.com.myapplication.di.module.RepositoryModule
import n.com.myapplication.di.module.NetWorkModule
import n.com.myapplication.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
      AndroidInjectionModule::class,
      AppModule::class,
      NetWorkModule::class,
      RepositoryModule::class,
      ActivityBuildersModule::class]
)

interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }

  fun inject(mainApplication: MainApplication)
}