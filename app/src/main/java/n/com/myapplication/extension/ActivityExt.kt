/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package n.com.myapplication.extension

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Various extension functions for AppCompatActivity.
 */


const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3

fun AppCompatActivity.replaceFragmentInActivity(@IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = false, tag: String = fragment::class.java.simpleName) {
  supportFragmentManager.transact {
    if (addToBackStack) {
      addToBackStack(tag)
    }
    replace(containerId, fragment, tag)
  }

  fun AppCompatActivity.addFragmentToActivity(@IdRes containerId: Int, fragment: Fragment,
      addToBackStack: Boolean = false, tag: String = fragment::class.java.simpleName) {
    supportFragmentManager.transact {
      if (addToBackStack) {
        addToBackStack(tag)
      }
      add(fragment, tag)
    }
  }

  fun AppCompatActivity.goBackFragment(): Boolean {
    val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 0
    if (isShowPreviousPage) {
      supportFragmentManager.popBackStackImmediate()
    }
    return isShowPreviousPage
  }

  fun AppCompatActivity.startActivity(@NonNull intent: Intent,
      flags: Int? = null) {
    flags.notNull {
      intent.flags = it
    }
    startActivity(intent)
  }

  fun AppCompatActivity.startActivityForResult(@NonNull intent: Intent,
      requestCode: Int, flags: Int? = null) {
    flags.notNull {
      intent.flags = it
    }
    startActivityForResult(intent, requestCode)
  }

}
