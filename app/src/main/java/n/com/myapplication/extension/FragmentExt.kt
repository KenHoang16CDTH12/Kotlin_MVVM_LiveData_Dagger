package n.com.myapplication.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun Fragment.replaceFragment(@IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = false, tag: String = fragment::class.java.simpleName) {
  fragmentManager?.transact {
    if (addToBackStack) {
      addToBackStack(tag)
    }
    replace(containerId, fragment, tag)
  }
}

fun Fragment.addFragment(fragment: Fragment, addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName) {
  fragmentManager?.transact {
    if (addToBackStack) {
      addToBackStack(tag)
    }
    add(fragment, tag)
  }
}

fun Fragment.goBackFragment(): Boolean {
  fragmentManager.notNull {
    val isShowPreviousPage = it.backStackEntryCount > 0
    if (isShowPreviousPage) {
      it.popBackStackImmediate()
    }
    return isShowPreviousPage
  }
  return false
}

/**
 * Runs a FragmentTransaction, then calls commitAllowingStateLoss().
 */
inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
  beginTransaction().apply {
    action()
  }.commitAllowingStateLoss()
}
