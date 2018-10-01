package n.com.myapplication

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import n.com.myapplication.di.AppInjector
import n.com.myapplication.util.GlideApp
import javax.inject.Inject


class MainApplication : Application(), HasActivityInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()

    sInstance = this

    AppInjector.init(this)

    configLeakCanary()
  }

  override fun activityInjector() = dispatchingAndroidInjector

  override fun onLowMemory() {
    GlideApp.get(this).onLowMemory()
    super.onLowMemory()
  }

  override fun onTrimMemory(level: Int) {
    GlideApp.get(this).onTrimMemory(level)
    super.onTrimMemory(level)
  }

  private fun configLeakCanary() {
    if (BuildConfig.DEBUG) {
      if (LeakCanary.isInAnalyzerProcess(this)) {
        return
      }
      LeakCanary.install(this)
    }
  }

  companion object {
    lateinit var sInstance: MainApplication
  }

}