package bangkit.mobiledev.cek_apel.ui.article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val articleName = intent.getStringExtra("ARTICLE_NAME") ?: ""
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION") ?: ""
        val articleHandling = intent.getStringArrayListExtra("ARTICLE_HANDLING") ?: arrayListOf()

        binding.tvArticleTitle.text = articleName
        binding.tvArticleDescription.text = articleDescription

        // You might want to set up a RecyclerView or TextView to display handling steps
        val handlingStepsText = articleHandling.joinToString("\n") { it }
        binding.tvHandlingSteps.text = handlingStepsText
    }
}