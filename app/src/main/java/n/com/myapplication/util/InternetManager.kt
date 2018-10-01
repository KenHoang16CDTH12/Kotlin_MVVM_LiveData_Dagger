package n.com.myapplication.util

import android.content.Context
import android.net.ConnectivityManager


class InternetManager {

  companion object {
    fun isConnected(context: Context): Boolean {
      val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
      return cm?.activeNetworkInfo != null
    }
  }

}