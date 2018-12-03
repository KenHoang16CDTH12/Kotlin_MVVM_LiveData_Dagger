package n.com.myapplication.data.source.remote.api.middleware

import android.text.TextUtils
import androidx.annotation.NonNull
import n.com.myapplication.data.source.remote.api.error.RetrofitException
import com.ccc.jobchat.data.source.remote.api.error.ErrorResponse
import com.google.gson.Gson
import io.reactivex.*
import n.com.myapplication.util.LogUtils
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * ErrorHandling:
 * http://bytes.babbel.com/en/articles/2016-03-16-retrofit2-rxjava-error-handling.html
 * This class only for Call in retrofit 2
 */

class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {
  private val TAG = RxErrorHandlingCallAdapterFactory::class.java.name

  private val original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

  override fun get(returnType: Type, annotations: Array<Annotation>,
      retrofit: Retrofit): CallAdapter<*, *> {
    return RxCallAdapterWrapper(returnType,
        wrapped = original.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>)
  }

  /**
   * RxCallAdapterWrapper
   */
  internal inner class RxCallAdapterWrapper<R>(private val returnType: Type,
      private val wrapped: CallAdapter<R, Any>) : CallAdapter<R, Any> {

    override fun responseType(): Type {
      return wrapped.responseType()
    }

    override fun adapt(@NonNull call: Call<R>): Any? {
      val rawType = CallAdapter.Factory.getRawType(returnType)

      val isFlowable = rawType == Flowable::class.java
      val isSingle = rawType == Single::class.java
      val isMaybe = rawType == Maybe::class.java
      val isCompletable = rawType == Completable::class.java

      if (rawType != Observable::class.java && !isFlowable && !isSingle && !isMaybe) {
        return null
      }
      if (returnType !is ParameterizedType) {
        val name = if (isFlowable)
          "Flowable"
        else if (isSingle) "Single" else if (isMaybe) "Maybe" else "Observable"
        throw IllegalStateException(name
            + " return type must be parameterized"
            + " as "
            + name
            + "<Foo> or "
            + name
            + "<? extends Foo>")
      }


      if (isFlowable) {
        return (wrapped.adapt(call) as Flowable<*>).onErrorResumeNext { throwable: Throwable ->
          Flowable.error(convertToBaseException(throwable))
        }
      }
      if (isSingle) {
        return (wrapped.adapt(call) as Single<*>).onErrorResumeNext { throwable ->
          Single.error(convertToBaseException(throwable))
        }
      }
      if (isMaybe) {
        return (wrapped.adapt(call) as Maybe<*>).onErrorResumeNext { throwable: Throwable ->
          Maybe.error(convertToBaseException(throwable))
        }
      }
      if (isCompletable) {
        return (wrapped.adapt(call) as Completable).onErrorResumeNext { throwable ->
          Completable.error(convertToBaseException(throwable))
        }
      }
      return (wrapped.adapt(call) as Observable<*>).onErrorResumeNext { throwable: Throwable ->
        Observable.error(convertToBaseException(throwable))
      }
    }

    private fun convertToBaseException(throwable: Throwable): RetrofitException {
      if (throwable is RetrofitException) {
        return throwable
      }

      // A network error happened
      if (throwable is IOException) {
        return RetrofitException.toNetworkError(throwable)
      }

      // We had non-200 http error
      if (throwable is HttpException) {
        val response = throwable.response()
        if (response.errorBody() != null) {
          return try {
            val errorResponse = Gson().fromJson(response.errorBody()?.string(),
                ErrorResponse::class.java)

            if (errorResponse != null && !TextUtils.isEmpty(errorResponse.error.message)) {
              RetrofitException.toServerError(errorResponse)
            } else {
              RetrofitException.toHttpError(response)
            }

          } catch (e: IOException) {
            LogUtils.e(TAG, e.localizedMessage)
            RetrofitException.toUnexpectedError(throwable)
          }
        } else {
          return RetrofitException.toHttpError(response)
        }
      }

      // We don't know what happened. We need to simply convert to an unknown error
      return RetrofitException.toUnexpectedError(throwable)
    }
  }

  companion object {

    fun create(): CallAdapter.Factory {
      return RxErrorHandlingCallAdapterFactory()
    }
  }
}