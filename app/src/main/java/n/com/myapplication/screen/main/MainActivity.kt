package n.com.myapplication.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import n.com.myapplication.R
import n.com.myapplication.base.BaseActivity
import n.com.myapplication.databinding.ActivityMainBinding
import n.com.myapplication.extension.addFragmentToActivity
import n.com.myapplication.extension.switchFragment
import n.com.myapplication.screen.blank.BlankFragment
import n.com.myapplication.screen.empty.EmptyFragment
import n.com.myapplication.screen.user.UserFragment


class MainActivity : BaseActivity() {

  private lateinit var viewModel: MainActivityViewModel

  private lateinit var currentFragment: Fragment
  private var userFragment = UserFragment()
  private val blankFragment: BlankFragment by lazy { BlankFragment.newInstance() }
  private val emptyFragment: EmptyFragment by lazy { EmptyFragment.newInstance() }

  private var handler: Handler? = null
  private var runnable: Runnable? = null
  private var isDoubleTapBack = false

  override fun onCreateView(savedInstanceState: Bundle?) {
    viewModel = MainActivityViewModel.create(this, viewModelFactory)
    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    binding.viewModel = viewModel
  }

  override fun onBackPressed() {
    if (viewModel.currentTab != TAB1) {
      bottomNav.selectedItemId = R.id.tab1
      switchTab(TAB1, userFragment)
      return
    }
    if (isDoubleTapBack) {
      finish()
      return
    }
    isDoubleTapBack = true
    Toast.makeText(this, "please click again to exit", Toast.LENGTH_SHORT).show()
    handler?.postDelayed(runnable, DELAY_TIME_TWO_TAP_BACK_BUTTON.toLong())
  }

  override fun onDestroy() {
    super.onDestroy()
    handler?.removeCallbacks(runnable)
  }

  override fun setUpView() {
    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.tab1 -> switchTab(TAB1, userFragment)
        R.id.tab2 -> switchTab(TAB2, blankFragment)
        R.id.tab3 -> switchTab(TAB3, userFragment)
        R.id.tab4 -> switchTab(TAB4, emptyFragment)
        R.id.tab5 -> switchTab(TAB5, userFragment)
      }
      return@OnNavigationItemSelectedListener true
    }
    bottomNav.selectedItemId = R.id.tab1
    bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
  }

  override fun bindView() {
    handler = Handler()
    runnable = Runnable { isDoubleTapBack = false }

    addFragmentToActivity(R.id.mainContainer, userFragment)
    currentFragment = userFragment
  }

  private fun switchTab(currentTab: Int, fragment: Fragment) {
    if (viewModel.currentTab == currentTab) {
      return
    }
    viewModel.currentTab = currentTab
    switchFragment(R.id.mainContainer, currentFragment = currentFragment, newFragment = fragment)
    currentFragment = fragment
  }

  companion object {
    fun getInstance(context: Context): Intent {
      return Intent(context, MainActivity::class.java)
    }

    const val DELAY_TIME_TWO_TAP_BACK_BUTTON = 2000

    const val TAB1 = 0
    const val TAB2 = 1
    const val TAB3 = 2
    const val TAB4 = 3
    const val TAB5 = 4
  }
}
