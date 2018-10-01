package n.com.myapplication.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import n.com.myapplication.R
import n.com.myapplication.base.BaseActivity
import n.com.myapplication.databinding.ActivityMainBinding
import n.com.myapplication.extension.replaceFragmentInActivity
import n.com.myapplication.screen.user.UserFragment


class MainActivity : BaseActivity() {

  private lateinit var viewModel: MainActivityViewModel

  override fun onCreateView(savedInstanceState: Bundle?) {
    viewModel = MainActivityViewModel.create(this, viewModelFactory)
    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    binding.viewModel = viewModel
  }

  override fun setUpView() {
    replaceFragmentInActivity(containerId = R.id.container, fragment = UserFragment.newInstance(),
        tag = UserFragment.TAG)
  }

  override fun bindView() {
    //No-Op
  }

  companion object {
    fun getInstance(context: Context): Intent {
      return Intent(context, MainActivity::class.java)
    }
  }
}
