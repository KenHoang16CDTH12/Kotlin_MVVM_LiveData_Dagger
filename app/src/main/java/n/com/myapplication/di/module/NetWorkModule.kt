package n.com.myapplication.di.module

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import n.com.myapplication.BuildConfig
import n.com.myapplication.data.source.remote.api.middleware.InterceptorImpl
import n.com.myapplication.data.source.remote.api.middleware.RxErrorHandlingCallAdapterFactory
import n.com.myapplication.data.source.remote.service.AppApi
import n.com.myapplication.data.source.repositories.TokenRepository
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetWorkModule {

    companion object {
        private const val READ_TIMEOUT: Long = 30
        private const val WRITE_TIMEOUT: Long = 30
        private const val CONNECTION_TIMEOUT: Long = 30
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpCache(app: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
        return Cache(app.cacheDir, cacheSize)
    }

    @Singleton
    @Provides
    fun provideInterceptor(tokenRepository: TokenRepository): Interceptor {
        return InterceptorImpl(tokenRepository)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.cache(cache)
        httpClientBuilder.addInterceptor(interceptor)

        httpClientBuilder.readTimeout(
                READ_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(
                WRITE_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(
                CONNECTION_TIMEOUT, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            httpClientBuilder.addInterceptor(logging)
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        return httpClientBuilder.build()
    }


    @Singleton
    @Provides
    fun provideNameApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }
}