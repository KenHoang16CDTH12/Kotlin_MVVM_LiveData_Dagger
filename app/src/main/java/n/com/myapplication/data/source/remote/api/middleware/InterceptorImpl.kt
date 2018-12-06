package n.com.myapplication.data.source.remote.api.middleware

import androidx.annotation.NonNull
import n.com.myapplication.util.extension.notNull
import n.com.myapplication.repositories.TokenRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class InterceptorImpl(private var mTokenRepository: TokenRepository?) : Interceptor {

  companion object {
    private const val TAG = "InterceptorImpl"
    private const val TOKEN_TYPE = "Bearer "
    private const val KEY_TOKEN = "Authorization"
  }

  private var mIsRefreshToken: Boolean = false

  @Throws(IOException::class)
  override fun intercept(@NonNull chain: Interceptor.Chain): Response? {
    //TODO check connection

    val builder = initializeHeader(chain)
    var request = builder.build()
    var response = chain.proceed(request)

    if (!mIsRefreshToken && response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

      //TODO refresh Token

      builder.removeHeader(KEY_TOKEN)

      mTokenRepository?.getToken()?.accessToken.notNull { accessToken ->
        builder.addHeader(KEY_TOKEN, TOKEN_TYPE + accessToken)
        request = builder.build()
        response = chain.proceed(request)
      }
    }

    return response
  }

  private fun initializeHeader(chain: Interceptor.Chain): Request.Builder {
    val originRequest = chain.request()
    val builder = originRequest.newBuilder()
        .header("Accept", "application/json")
        .addHeader("Cache-Control", "no-cache")
        .addHeader("Cache-Control", "no-store")
        .method(originRequest.method(), originRequest.body())


    mTokenRepository?.getToken()?.accessToken.notNull { accessToken ->
      builder.addHeader(KEY_TOKEN, TOKEN_TYPE + accessToken)
    }

    return builder
  }

  //TODO refreshToken
  /* private fun refreshToken() {
     if (mTokenRepository == null) {
       return
     }
     mIsRefreshToken = true
     mTokenRepository!!.refreshToken().subscribe(object : DisposableSingleObserver<LoginResponse>() {
       override fun onSuccess(loginResponse: LoginResponse?) {
         if (loginResponse != null && loginResponse!!.getToken() != null) {
           mTokenRepository!!.saveToken(loginResponse!!.getToken())
         }
         mIsRefreshToken = false
       }

       override fun onError(e: Throwable) {
         LogUtils.e(TAG, "refreshToken", e)
         mIsRefreshToken = false
       }
     })
   }*/
}