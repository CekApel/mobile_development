package bangkit.mobiledev.cek_apel.data.retrofit

import bangkit.mobiledev.cek_apel.data.response.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("artikels")
    suspend fun getArticles(): Response<ArticleResponse>
}
