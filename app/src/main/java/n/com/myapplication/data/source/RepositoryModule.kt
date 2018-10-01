package n.com.myapplication.data.source

import android.app.Application
import dagger.Module
import dagger.Provides
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsApi
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsImpl
import n.com.myapplication.data.source.remote.service.AppApi
import n.com.myapplication.repositories.TokenRepository
import n.com.myapplication.repositories.UserRepository
import n.com.myapplication.repositories.UserRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {


  @Singleton
  @Provides
  fun provideTokenRepository(app: Application): TokenRepository {
    return TokenRepository(SharedPrefsImpl(app))
  }

  @Provides
  @Singleton
  fun provideUserRepository(api: AppApi, sharedPrefsApi: SharedPrefsApi): UserRepository {
    return UserRepositoryImpl(api, sharedPrefsApi)
  }

}