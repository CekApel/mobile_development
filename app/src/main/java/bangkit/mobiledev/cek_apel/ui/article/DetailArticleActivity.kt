package bangkit.mobiledev.cek_apel.ui.article

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

        val articleName = intent.getStringExtra("ARTICLE_NAME") ?: ""
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION") ?: ""
        val articleHandling = intent.getStringArrayListExtra("ARTICLE_HANDLING") ?: arrayListOf("Tidak ada informasi penanganan")
        val articleImageUrl = intent.getStringExtra("ARTICLE_IMAGE_URL") ?: ""

        binding.apply {
            tvArticleTitle.text = articleName
            tvArticleDescription.text = articleDescription

            // Load image using Glide
            Glide.with(this@DetailArticleActivity)
                .load(articleImageUrl)
                .into(ivArticle)

            // Display handling steps
            val handlingStepsText = articleHandling?.mapIndexed { index, step ->
                "${index + 1}. $step"
            }?.joinToString("\n") ?: "Tidak ada informasi penanganan"
            tvHandlingSteps.text = handlingStepsText
        }

        // Optional: Add back button functionality
//        binding.btnBack.setOnClickListener { finish() }
    }
}
