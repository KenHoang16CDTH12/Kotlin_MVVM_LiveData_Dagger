package n.com.myapplication.screen.main

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import n.com.myapplication.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel
@Inject constructor() : BaseViewModel() {

  var currentTab = MainActivity.TAB1

  companion object {
    fun create(activity: FragmentActivity,
        factory: ViewModelProvider.Factory): MainActivityViewModel {
      return ViewModelProvider(activity, factory).get(MainActivityViewModel::class.java)
    }
  }
}