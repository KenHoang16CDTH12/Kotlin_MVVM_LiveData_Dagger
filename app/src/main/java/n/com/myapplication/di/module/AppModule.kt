package n.com.myapplication.di.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import n.com.myapplication.data.source.local.dao.AppDatabase
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsApi
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsImpl
import n.com.myapplication.data.source.remote.api.middleware.BooleanAdapter
import n.com.myapplication.data.source.remote.api.middleware.DoubleAdapter
import n.com.myapplication.data.source.remote.api.middleware.IntegerAdapter
import n.com.myapplication.util.rxAndroid.BaseSchedulerProvider
import n.com.myapplication.util.rxAndroid.SchedulerProvider
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun provideResources(app: Application): Resources {
        return app.resources
    }

    @Provides
    @Singleton
    fun provideSharedPrefsApi(app: Application): SharedPrefsApi {
        return SharedPrefsImpl(app)
    }

    @Singleton
    @Provides
    fun provideBaseSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider()
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

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app.applicationContext, AppDatabase::class.java,
                DB_NAME)
                .fallbackToDestructiveMigration().build()
    }

    companion object {
        const val DB_NAME = "db_name"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //No-Op
            }
        }
    }

}
