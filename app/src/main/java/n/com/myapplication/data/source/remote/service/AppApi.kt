package n.com.myapplication.data.source.remote.service

import io.reactivex.Single
import n.com.myapplication.data.source.remote.api.response.RepoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface AppApi {

  @GET("/search/repositories")
  fun searchRepository(@Query("q") query: String?, @Query(
      "page") page: Int): Single<RepoSearchResponse>

}