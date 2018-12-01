package n.com.myapplication.util

import android.util.Log
import n.com.myapplication.BuildConfig

object LogUtils {

  private val DEBUG = BuildConfig.DEBUG

  fun d(tag: String, message: String) {
    if (DEBUG) {
      Log.d(tag, message)
    }
  }

  fun e(tag: String, msg: String) {
    if (DEBUG) {
      Log.e(tag, msg)
    }
  }

  fun e(tag: String, msg: String, throwable: Throwable) {
    if (DEBUG) {
      Log.e(tag, msg, throwable)
    }
  }
}