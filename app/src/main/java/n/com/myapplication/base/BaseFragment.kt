package n.com.myapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import n.com.myapplication.di.Injectable
import javax.inject.Inject

abstract class BaseFragment : Fragment(), Injectable {
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return createView(inflater, container, savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpView()
    bindView()
  }

  fun showLoading() {
    if (activity is BaseActivity) {
      (activity as BaseActivity).showLoading()
    }
  }

  fun hideLoading() {
    if (activity is BaseActivity) {
      (activity as BaseActivity).hideLoading()
    }
  }

  protected abstract fun createView(@NonNull inflater: LayoutInflater,
      @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View

  protected abstract fun setUpView()

  protected abstract fun bindView()
}