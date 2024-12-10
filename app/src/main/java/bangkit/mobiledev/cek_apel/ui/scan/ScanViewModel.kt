package bangkit.mobiledev.cek_apel.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bangkit.mobiledev.cek_apel.data.response.ModelMLResponse
import bangkit.mobiledev.cek_apel.data.retrofit.ApiConfig
import bangkit.mobiledev.cek_apel.utils.reduceFileImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ScanViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _predictionResult = MutableLiveData<Result<ModelMLResponse>>()
    val predictionResult: LiveData<Result<ModelMLResponse>> = _predictionResult

    fun predictImage(imageFile: File, description: String = "Apple classification") {
        viewModelScope.launch {
            try {
                val compressedFile = imageFile.reduceFileImage()

                val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "image",
                    compressedFile.name,
                    requestImageFile
                )

                val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())

                val response = apiService.predictImage(imageMultipart, descriptionRequestBody)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _predictionResult.value = Result.success(it)
                    } ?: run {
                        _predictionResult.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    _predictionResult.value = Result.failure(
                        Exception("Error: ${response.code()} - ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _predictionResult.value = Result.failure(e)
            }
        }
    }
}
