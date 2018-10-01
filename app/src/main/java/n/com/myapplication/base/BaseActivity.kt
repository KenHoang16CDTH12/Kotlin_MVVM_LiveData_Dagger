package n.com.myapplication.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import n.com.myapplication.extension.isNull
import n.com.myapplication.extension.notNull
import n.com.myapplication.widget.DialogManager.DialogManager
import n.com.myapplication.widget.DialogManager.DialogManagerImpl
import javax.inject.Inject

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  private val lockObj = Any()

  private var dialogManager: DialogManager? = null

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return dispatchingAndroidInjector
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    onCreateView(savedInstanceState)
    setUpView()
    bindView()
  }

  override fun onDestroy() {
    dialogManager.notNull {
      dialogManager?.onRelease()
      dialogManager = null
    }
    super.onDestroy()
  }


  fun showLoading() {
    dialogManager.isNull {
      dialogManager = DialogManagerImpl(this)
    }
    runOnUiThread {
      synchronized(lockObj) {
        if (isFinishing) {
          return@runOnUiThread
        }
        dialogManager?.showProgressDialog()
      }
    }
  }

  fun hideLoading() {
    dialogManager.isNull {
      dialogManager = DialogManagerImpl(this)
    }
    runOnUiThread {
      synchronized(lockObj) {
        if (isFinishing) {
          return@runOnUiThread
        }
        dialogManager?.hideProgressDialog()
      }
    }
  }

  protected abstract fun onCreateView(savedInstanceState: Bundle?)

  protected abstract fun setUpView()

  protected abstract fun bindView()

}