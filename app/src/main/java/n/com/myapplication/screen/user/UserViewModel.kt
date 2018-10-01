package n.com.myapplication.screen.user

import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import n.com.myapplication.data.source.remote.api.error.BaseException
import io.reactivex.Observable
import n.com.myapplication.base.BaseViewModel
import n.com.myapplication.data.model.User
import n.com.myapplication.extension.isNull
import n.com.myapplication.extension.notNull
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

  var repoList = MutableLiveData<MutableList<User>>()

  var isError = MutableLiveData<BaseException>()
  var isLoading = MutableLiveData<Boolean>()
  var isLoadMore = MutableLiveData<Boolean>()
  var isRefresh = MutableLiveData<Boolean>()

  var query = MutableLiveData<String>()
  var currentPage = Constant.PAGE_DEFAULT

  fun searchUser(isLoading: Boolean = false, isLoadMore: Boolean = false,
      isRefresh: Boolean = false) {

    query.value.isNull {
      return@isNull
    }

    currentPage += 1

    if (isLoading || isRefresh) {
      currentPage = Constant.PAGE_DEFAULT

    }

    val composite = userRepository.searchRepository(query.value, currentPage)
        .subscribeOn(baseSchedulerProvider.io())
        .doOnSubscribe {
          if (isLoading) {
            this.isLoading.value = true
            return@doOnSubscribe
          }
          if (isLoadMore) {
            this.isLoadMore.value = true
            return@doOnSubscribe
          }
          if (isRefresh) {
            this.isRefresh.value = true
          }
        }
        .observeOn(baseSchedulerProvider.ui())
        .doAfterTerminate {
          if (isLoading) {
            this.isLoading.value = false
            return@doAfterTerminate
          }
          if (isRefresh) {
            this.isRefresh.value = false
          }
        }
        .subscribe(
            { data ->
              if (data.isEmpty()) {
                return@subscribe
              }

              if (isLoading || isRefresh) {
                repoList.value = data.toMutableList()
                return@subscribe
              }

              if (isLoadMore) {
                this.isLoadMore.value = false

                repoList.value.notNull { value ->
                  value.addAll(data)
                  repoList.value = value
                }
              }
            },
            { throwable ->
              if (isLoadMore) {
                this.isLoadMore.value = false
              }

              if (throwable is BaseException) {
                isError.value = throwable
              }
            })
    compositeDisposable.add(composite)
  }

  fun initRxSearch(editText: EditText) {
    val composite = RxView.search(editText)
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .filter { query ->
          !query.isEmpty()
        }
        .switchMap { query ->
          currentPage = Constant.PAGE_DEFAULT
          userRepository.searchRepository(query, currentPage)
              .toObservable()
              .onErrorResumeNext(Observable.empty())
        }
        .subscribeOn(baseSchedulerProvider.io())
        .observeOn(baseSchedulerProvider.ui())
        .subscribe(
            { data ->
              if (!data.isEmpty()) {
                repoList.value = data.toMutableList()
              }
            },
            { throwable ->
              if (throwable is BaseException) {
                isError.value = throwable
              }
            })
    compositeDisposable.add(composite)
  }

  companion object {
    fun create(fragment: Fragment,
        factory: ViewModelProvider.Factory): UserViewModel {
      return ViewModelProvider(fragment, factory).get(UserViewModel::class.java)
    }
  }
}