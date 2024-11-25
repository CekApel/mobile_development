package bangkit.mobiledev.cek_apel.data.retrofit

import bangkit.mobiledev.cek_apel.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("article")
    fun getArticle(): Call<ArticleResponse>
}