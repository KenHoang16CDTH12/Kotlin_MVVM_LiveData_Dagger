package n.com.myapplication.di.module

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import n.com.myapplication.data.source.local.dao.AppDatabase
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsApi
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsImpl
import n.com.myapplication.data.source.remote.service.AppApi
import n.com.myapplication.data.source.repositories.*
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideTokenRepository(app: Application): TokenRepository {
        return TokenRepository(SharedPrefsImpl(app))
    }

    @Singleton
    @Provides
    fun provideUserRepository(api: AppApi, sharedPrefsApi: SharedPrefsApi): UserRepository {
        return UserRepositoryImpl(api, sharedPrefsApi)
    }

    @Singleton
    @Provides
    fun provideAppDBRepository(appDatabase: AppDatabase, gson: Gson): AppDBRepository {
        return AppDBRepositoryImpl(appDatabase, gson)
    }

}