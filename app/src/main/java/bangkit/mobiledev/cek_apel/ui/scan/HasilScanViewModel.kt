package bangkit.mobiledev.cek_apel.ui.scan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import bangkit.mobiledev.cek_apel.data.response.ModelMLResponse
import bangkit.mobiledev.cek_apel.data.retrofit.ApiConfig
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity
import bangkit.mobiledev.cek_apel.utils.reduceFileImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HasilScanViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiConfig.getApiService()
    private val historyScanViewModel = HistoryScanViewModel(application)

    private val _predictionResult = MutableLiveData<Result<ModelMLResponse>>()
    val predictionResult: LiveData<Result<ModelMLResponse>> = _predictionResult

    fun predictImage(imageFile: File, description: String = "Apple classification") {
        viewModelScope.launch {
            try {
                // Compress the image file
                val compressedFile = imageFile.reduceFileImage()

                // Prepare image multipart
                val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "image",
                    compressedFile.name,
                    requestImageFile
                )

                // Prepare description
                val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())

                // Get current timestamp
                val currentDateTime = getCurrentDateTime()

                // Make API call
                val response = apiService.predictImage(imageMultipart, descriptionRequestBody)

                if (response.isSuccessful) {
                    response.body()?.let { mlResponse ->

                        val scanHistory = ScanHistoryEntity(
                            imageUri = imageFile.toString(),
                            result = mlResponse.data.result,
                            confidenceScore = mlResponse.data.confidenceScore,
                            scanDate = getCurrentDateTime(),
                            explanation = mlResponse.data.explanation,
                            suggestion = mlResponse.data.suggestion,
                            medicine = mlResponse.data.medicine
                        )

                        // Inject HistoryScanViewModel or repository to save
                        historyScanViewModel.insertScanHistory(scanHistory)

                        mlResponse.data.createdAt = currentDateTime
                        _predictionResult.value = Result.success(mlResponse)


                        // Optionally override the created at timestamp from the server
                        mlResponse.data.createdAt = currentDateTime
                        _predictionResult.value = Result.success(mlResponse)
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

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}