package n.com.myapplication.rxAndroid

import androidx.annotation.NonNull
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class ImmediateSchedulerProvider : BaseSchedulerProvider {

  @NonNull
  override fun computation(): Scheduler {
    return Schedulers.trampoline()
  }

  @NonNull
  override fun io(): Scheduler {
    return Schedulers.trampoline()
  }

  @NonNull
  override fun ui(): Scheduler {
    return Schedulers.trampoline()
  }
}