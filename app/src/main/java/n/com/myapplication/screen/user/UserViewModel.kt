package n.com.myapplication.screen.user

import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import n.com.myapplication.base.BaseViewModel
import n.com.myapplication.data.model.User
import n.com.myapplication.data.source.remote.api.error.RetrofitException
import n.com.myapplication.extension.notNull
import n.com.myapplication.extension.with
import n.com.myapplication.liveData.Resource
import n.com.myapplication.liveData.SingleLiveEvent
import n.com.myapplication.liveData.Status
import n.com.myapplication.repositories.UserRepository
import n.com.myapplication.rxAndroid.BaseSchedulerProvider
import n.com.myapplication.util.Constant
import n.com.myapplication.util.RxView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserViewModel
@Inject constructor(
    private val baseSchedulerProvider: BaseSchedulerProvider,
    private val userRepository: UserRepository) : BaseViewModel() {

  var query = MutableLiveData<String>()
  var repoList = SingleLiveEvent<Resource<MutableList<User>>>()

  fun searchUser(status: Status, page: Int = Constant.PAGE_DEFAULT) {
    launchDisposable {
      userRepository.searchRepository(query.value, page)
          .map { response ->
            val data = repoList.value?.data
            data.notNull { it ->
              if (status == Status.REFRESH_DATA) {
                it.clear()
              }
              it.addAll(response)
              return@map data
            }
            return@map response.toMutableList()
          }
          .with(baseSchedulerProvider)
          .subscribe(
              { data ->
                repoList.value = Resource.multiStatus(status, data)
              },
              { throwable ->
                if (throwable is RetrofitException) {
                  repoList.value = Resource.error(throwable)
                }
              })
    }
  }

  fun initRxSearch(editText: EditText) {
    launchDisposable {
      RxView.search(editText).skip(1)
          .debounce(300, TimeUnit.MILLISECONDS)
          .distinctUntilChanged()
          .switchMap { query ->
            userRepository.searchRepository(query, Constant.PAGE_DEFAULT)
                .toObservable()
                .onErrorResumeNext(Observable.empty())
          }
          .with(baseSchedulerProvider)
          .subscribe(
              { data ->
                repoList.value = Resource.searchData(data.toMutableList())
              },
              { throwable ->
                initRxSearch(editText)
                if (throwable is RetrofitException) {
                  repoList.value = Resource.error(throwable)
                }
              })
    }
  }

  companion object {
    fun create(fragment: Fragment,
        factory: ViewModelProvider.Factory): UserViewModel {
      return ViewModelProvider(fragment, factory).get(UserViewModel::class.java)
    }
  }
}