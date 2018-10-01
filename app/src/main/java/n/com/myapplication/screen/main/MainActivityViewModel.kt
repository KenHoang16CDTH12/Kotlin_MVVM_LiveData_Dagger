package n.com.myapplication.screen.main

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import n.com.myapplication.base.BaseViewModel
import n.com.myapplication.repositories.UserRepository
import n.com.myapplication.rxAndroid.BaseSchedulerProvider
import javax.inject.Inject

class MainActivityViewModel
@Inject constructor(
    private val baseSchedulerProvider: BaseSchedulerProvider,
    private val userRepository: UserRepository) : BaseViewModel() {

  companion object {
    fun create(activity: FragmentActivity,
        factory: ViewModelProvider.Factory): MainActivityViewModel {
      return ViewModelProvider(activity, factory).get(MainActivityViewModel::class.java)
    }
  }

}