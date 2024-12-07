package bangkit.mobiledev.cek_apel.ui.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import bangkit.mobiledev.cek_apel.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val toolbar = binding.topAppBar
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val articleName = intent.getStringExtra("ARTICLE_NAME") ?: ""
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION") ?: ""
        val articleHandling = intent.getStringArrayListExtra("ARTICLE_HANDLING") ?: arrayListOf("Tidak ada informasi penanganan")
        val articleImageUrl = intent.getStringExtra("ARTICLE_IMAGE_URL") ?: ""

        binding.apply {
            tvArticleTitle.text = articleName
            tvArticleDescription.text = articleDescription
            Glide.with(this@DetailArticleActivity)
                .load(articleImageUrl)
                .into(ivArticle)

            val handlingStepsText = articleHandling?.mapIndexed { index, step -> "${index + 1}. $step" }?.joinToString("\n") ?: "Tidak ada informasi penanganan"
            tvHandlingSteps.text = handlingStepsText
        }

        // Setup Share button click listener
        binding.btnShare.setOnClickListener {
            shareArticle(articleName, articleDescription, articleHandling)
        }
    }

    private fun shareArticle(articleName: String, articleDescription: String, articleHandling: ArrayList<String>) {
        // Format the share text to include handling steps with automatic numbering
        val handlingText = articleHandling.mapIndexed { index, step -> "${index + 1}. $step" }.joinToString("\n")
        val shareText = """
             $articleName
             
             $articleDescription
             
            Handling Steps:
            $handlingText
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Share article via"))
    }
}
