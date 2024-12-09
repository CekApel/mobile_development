package bangkit.mobiledev.cek_apel.data.retrofit

import bangkit.mobiledev.cek_apel.data.response.ArticleResponse
import bangkit.mobiledev.cek_apel.data.response.ModelMLResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("artikels")
    suspend fun getArticles(): Response<ArticleResponse>

    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ModelMLResponse>
}
