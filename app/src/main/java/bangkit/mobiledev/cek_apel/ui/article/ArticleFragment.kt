package bangkit.mobiledev.cek_apel.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.mobiledev.cek_apel.adapter.ArticleAdapter
import bangkit.mobiledev.cek_apel.databinding.FragmentArticleBinding
import com.google.gson.JsonParser

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeArticles()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter { article ->
            // Parse penangananPenyakit
            val handlingList = parsePenangananPenyakit(article.penangananPenyakit)

            val intent = Intent(requireContext(), DetailArticleActivity::class.java).apply {
                putExtra("ARTICLE_NAME", article.nama)
                putExtra("ARTICLE_DESCRIPTION", article.deskripsi)
                putStringArrayListExtra("ARTICLE_HANDLING", ArrayList(handlingList))
                putExtra("ARTICLE_IMAGE_URL", article.imageUrl)
            }
            startActivity(intent)
        }

        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeArticles() {
        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
            binding.tvNoArticles.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        articleViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            binding.tvNoArticles.visibility = View.VISIBLE
            binding.tvNoArticles.text = errorMessage
        }

        articleViewModel.fetchArticles()
    }

    // Fungsi untuk mengonversi penangananPenyakit dari String menjadi List<String>
    private fun parsePenangananPenyakit(data: String): List<String> {
        return try {
            // Jika string adalah JSON array
            val jsonArray = JsonParser.parseString(data).asJsonArray
            jsonArray.map { it.asString }
        } catch (e: Exception) {
            // Jika bukan JSON array, pisahkan berdasarkan koma atau garis baru
            data.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }
}
