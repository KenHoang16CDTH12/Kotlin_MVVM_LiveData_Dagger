package n.com.myapplication.repositories

import com.google.gson.Gson
import io.reactivex.Single
import n.com.myapplication.data.model.User
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsApi
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsKey
import n.com.myapplication.data.source.remote.service.AppApi

interface UserRepository {

  fun saveUserToLocal(user: User)

  fun getUserFromLocal(): User?

  fun searchRepository(query: String?, page: Int): Single<List<User>>

}

class UserRepositoryImpl
constructor(private val api: AppApi, private val sharedPrefsApi: SharedPrefsApi) : UserRepository {

  private val gson = Gson()

  val user = User()

  override fun saveUserToLocal(user: User) {
    val data = gson.toJson(user)
    sharedPrefsApi.put(SharedPrefsKey.KEY_TOKEN, data)
  }

  override fun getUserFromLocal(): User? {
    return sharedPrefsApi.get(SharedPrefsKey.KEY_USER, User::class.java)
  }

  override fun searchRepository(query: String?, page: Int): Single<List<User>> {
    return api.searchRepository(query, page).map { value -> value.items }
  }

}
