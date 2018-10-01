package n.com.myapplication.data.source.local.sharedprf

interface SharedPrefsApi {
  fun <T> get(key: String, clazz: Class<T>): T

  fun <T> put(key: String, data: T)

  fun clear()
}