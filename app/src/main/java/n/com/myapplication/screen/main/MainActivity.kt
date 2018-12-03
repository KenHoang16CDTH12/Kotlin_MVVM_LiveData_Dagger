package n.com.myapplication.screen.main

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import n.com.myapplication.R
import n.com.myapplication.base.BaseActivity
import n.com.myapplication.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

  private lateinit var viewModel: MainActivityViewModel

  override fun onCreateView(savedInstanceState: Bundle?) {
    viewModel = MainActivityViewModel.create(this, viewModelFactory)
    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    binding.viewModel = viewModel
  }

  override fun setUpView() {
    val navHost: NavHostFragment = supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
    val navController = navHost.navController

    bottomNav?.setupWithNavController(navController)

    navController.addOnNavigatedListener { _, destination ->
      val dest: String = try {
        resources.getResourceName(destination.id)
      } catch (e: Resources.NotFoundException) {
        Integer.toString(destination.id)
      }
      Log.d("NavigationActivity", "Navigated to $dest")
    }
  }

  override fun bindView() {
    //No-Op
  }

  companion object {
    fun getInstance(context: Context): Intent {
      return Intent(context, MainActivity::class.java)
    }

    val options = navOptions {
      anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
      }
    }
  }
}
