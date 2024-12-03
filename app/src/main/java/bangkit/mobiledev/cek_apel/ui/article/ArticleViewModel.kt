package bangkit.mobiledev.cek_apel.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bangkit.mobiledev.cek_apel.data.response.DataItem
import bangkit.mobiledev.cek_apel.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {
    private val _articles = MutableLiveData<List<DataItem>>()
    val articles: LiveData<List<DataItem>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getArticles()

                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    Log.d("ArticleViewModel", "Fetched articles: $data")
                    _articles.value = data
                } else {
                    val errorMessage = "Failed to load articles: ${response.message()}"
                    Log.e("ArticleViewModel", errorMessage)
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Error fetching articles: ${e.message}"
                Log.e("ArticleViewModel", errorMessage, e)
                _error.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

}