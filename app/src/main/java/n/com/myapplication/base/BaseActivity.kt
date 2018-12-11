package n.com.myapplication.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import n.com.myapplication.MainApplication
import n.com.myapplication.data.source.remote.api.error.RetrofitException
import n.com.myapplication.util.extension.notNull
import n.com.myapplication.util.extension.showToast
import n.com.myapplication.widget.DialogManager.DialogManager
import n.com.myapplication.widget.DialogManager.DialogManagerImpl
import javax.inject.Inject

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    var dialogManager: DialogManager? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogManager = DialogManagerImpl(this)
        onCreateView(savedInstanceState)
        setUpView()
        bindView()
    }

    override fun onStart() {
        super.onStart()
        MainApplication.sInstance.setCurrentClass(javaClass)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager?.onRelease()
        dialogManager = null
    }

    fun onHandleError(error: RetrofitException?) {
        error?.getMessageError().notNull { showToast(it) }
    }

    protected abstract fun onCreateView(savedInstanceState: Bundle?)

    protected abstract fun setUpView()

    protected abstract fun bindView()

}