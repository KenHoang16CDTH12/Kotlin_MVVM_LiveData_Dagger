package n.com.myapplication.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

  private val compositeDisposable = CompositeDisposable()

  fun launchDisposable(job: () -> Disposable) {
    compositeDisposable.add(job())
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }

}