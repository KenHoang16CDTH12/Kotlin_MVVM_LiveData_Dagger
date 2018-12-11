package n.com.myapplication.data.source.repositories

import com.ccc.jobchat.data.model.Token
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsApi
import n.com.myapplication.data.source.local.sharedprf.SharedPrefsKey
import n.com.myapplication.util.extension.notNull

class TokenRepository
constructor(private val sharedPrefsApi: SharedPrefsApi) {

    private var tokenCache: Token? = null

    fun getToken(): Token? {
        tokenCache.notNull {
            return it
        }

        val token = sharedPrefsApi.get(SharedPrefsKey.KEY_TOKEN, Token::class.java)
        token.notNull {
            tokenCache = it
            return it
        }

        return null
    }


    fun saveToken(token: com.ccc.jobchat.data.model.Token) {
        tokenCache = token
        sharedPrefsApi.put(SharedPrefsKey.KEY_TOKEN, tokenCache)
    }

    fun clearToken() {
        tokenCache = null
        sharedPrefsApi.clear()
    }

}