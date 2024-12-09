package bangkit.mobiledev.cek_apel.data.response

import com.google.gson.annotations.SerializedName

data class ModelMLResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("createdAt")
	var createdAt: String,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Int,

	@field:SerializedName("suggestion")
	val suggestion: Any,

	@field:SerializedName("medicine")
	val medicine: Any,

	@field:SerializedName("explanation")
	val explanation: String
)
