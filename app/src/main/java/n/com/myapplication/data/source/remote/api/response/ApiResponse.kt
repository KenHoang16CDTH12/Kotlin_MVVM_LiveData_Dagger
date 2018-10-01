package n.com.myapplication.data.source.remote.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse<T> {
  @Expose
  @SerializedName("status")
  var status: Boolean? = null
  @Expose
  @SerializedName("data")
  var data: T? = null
}
