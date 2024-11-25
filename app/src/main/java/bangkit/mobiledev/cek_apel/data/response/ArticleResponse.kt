package bangkit.mobiledev.cek_apel.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<Any>,

	@field:SerializedName("status")
	val status: String
)
