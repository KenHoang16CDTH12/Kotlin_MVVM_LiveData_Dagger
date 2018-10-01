package n.com.myapplication.rxAndroid

import androidx.annotation.NonNull
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider private constructor() : BaseSchedulerProvider {

  companion object {
    val instance: BaseSchedulerProvider by lazy {
      SchedulerProvider()
    }
  }

  @NonNull
  override fun computation(): Scheduler {
    return Schedulers.computation()
  }

  @NonNull
  override fun io(): Scheduler {
    return Schedulers.io()
  }

  @NonNull
  override fun ui(): Scheduler {
    return AndroidSchedulers.mainThread()
  }


}