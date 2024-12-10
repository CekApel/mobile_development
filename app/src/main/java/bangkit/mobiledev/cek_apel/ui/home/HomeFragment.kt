package bangkit.mobiledev.cek_apel.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.adapter.ArticleAdapter
import bangkit.mobiledev.cek_apel.data.response.DataItem
import bangkit.mobiledev.cek_apel.databinding.FragmentHomeBinding
import bangkit.mobiledev.cek_apel.ui.article.DetailArticleActivity
import com.google.gson.JsonParser

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setupArticleRecyclerView()

        observeViewModel()

        homeViewModel.fetchArticles()

        binding.cardScan.setOnClickListener(this)
        binding.cardArticles.setOnClickListener(this)

        return root
    }

    private fun setupArticleRecyclerView() {
        articleAdapter = ArticleAdapter { article ->
            navigateToArticleDetail(article)
        }

        binding.rvRecentArticles.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }
    }

    private fun navigateToArticleDetail(article: DataItem) {
        val handlingList = parsePenangananPenyakit(article.penangananPenyakit)

        val intent = Intent(requireContext(), DetailArticleActivity::class.java).apply {
            putExtra("ARTICLE_NAME", article.nama)
            putExtra("ARTICLE_DESCRIPTION", article.deskripsi)
            putStringArrayListExtra("ARTICLE_HANDLING", ArrayList(handlingList))
            putExtra("ARTICLE_IMAGE_URL", article.imageUrl)
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        homeViewModel.articles.observe(viewLifecycleOwner) { articles ->
            val limitedArticles = articles.take(5)
            articleAdapter.submitList(limitedArticles)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parsePenangananPenyakit(data: String): List<String> {
        return try {
            val jsonArray = JsonParser.parseString(data).asJsonArray
            jsonArray.map { it.asString }
        } catch (e: Exception) {
            data.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        homeViewModel.articles.removeObservers(viewLifecycleOwner)
        homeViewModel.isLoading.removeObservers(viewLifecycleOwner)
        homeViewModel.error.removeObservers(viewLifecycleOwner)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_scan -> {
                findNavController().navigate(
                    R.id.action_home_to_scan,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, false)
                        .build()
                )
            }
            R.id.card_articles -> {
                findNavController().navigate(
                    R.id.action_home_to_article,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, false)
                        .build()
                )
            }
        }
    }
}