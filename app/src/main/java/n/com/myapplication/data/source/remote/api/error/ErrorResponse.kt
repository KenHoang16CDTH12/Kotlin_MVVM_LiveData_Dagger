package com.ccc.jobchat.data.source.remote.api.error

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import n.com.myapplication.util.extension.toStringWithFormatPattern
import n.com.myapplication.util.Constant

/**
 * Created by MyPC on 30/10/2017.
 */

class ErrorResponse {

  @Expose
  @SerializedName("error")
  private val mError: Error? = null

  val error: Error
    get() = mError ?: Error()

  inner class Error {
    @Expose
    @SerializedName("code")
    val code: Int = 0
    @Expose
    @SerializedName("description")
    private val messages: List<String>? = null

    val message: String?
      get() = if (messages == null || messages.isEmpty()) {
        ""
      } else {
        messages.toStringWithFormatPattern(Constant.ENTER_SPACE_FORMAT)
      }
  }
}
