package ksc.campus.tech.kakao.map.data.apis

import ksc.campus.tech.kakao.map.data.entities.KakaoSearchDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoSearchRetrofitService {
    @GET("/v2/local/search/keyword.json")
    suspend fun requestSearchResultByKeyword(
        @Header("Authorization") restApiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<KakaoSearchDTO>
}

