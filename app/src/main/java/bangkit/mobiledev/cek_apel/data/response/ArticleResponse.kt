package bangkit.mobiledev.cek_apel.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("penangananPenyakit")
	val penangananPenyakit: List<String>
)
