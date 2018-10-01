package n.com.myapplication.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import n.com.myapplication.MainApplication
import n.com.myapplication.data.source.RepositoryModule
import n.com.myapplication.data.source.remote.service.NetWorkModule
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