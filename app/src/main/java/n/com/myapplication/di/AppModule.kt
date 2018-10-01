package n.com.myapplication.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsApi
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsImpl
import n.com.myapplication.data.source.remote.api.middleware.BooleanAdapter
import n.com.myapplication.data.source.remote.api.middleware.DoubleAdapter
import n.com.myapplication.data.source.remote.api.middleware.IntegerAdapter
import n.com.myapplication.rxAndroid.BaseSchedulerProvider
import n.com.myapplication.rxAndroid.SchedulerProvider
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

  @Provides
  @Singleton
  fun provideContext(app: Application): Context {
    return app.applicationContext
  }

  @Provides
  @Singleton
  fun provideResources(app: Application): Resources {
    return app.resources
  }

  @Singleton
  @Provides
  fun provideSharedPrefsApi(app: Application): SharedPrefsApi {
    return SharedPrefsImpl(app)
  }

  @Singleton
  @Provides
  fun provideBaseSchedulerProvider(): BaseSchedulerProvider {
    return SchedulerProvider.instance
  }

  @Singleton
  @Provides
  fun provideGson(): Gson {
    val booleanAdapter = BooleanAdapter()
    val integerAdapter = IntegerAdapter()
    val doubleAdapter = DoubleAdapter()
    return GsonBuilder()
        .registerTypeAdapter(Boolean::class.java, booleanAdapter)
        .registerTypeAdapter(Int::class.java, integerAdapter)
        .registerTypeAdapter(Double::class.java, doubleAdapter)
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation()
        .create()
  }

}
