package bangkit.mobiledev.cek_apel.ui.article

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
                    _articles.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to load articles"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}