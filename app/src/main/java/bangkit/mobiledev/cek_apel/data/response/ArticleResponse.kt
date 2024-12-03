package bangkit.mobiledev.cek_apel.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("penanganan_penyakit")
	val penangananPenyakit: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String
)
