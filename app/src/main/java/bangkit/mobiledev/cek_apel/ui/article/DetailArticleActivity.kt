package bangkit.mobiledev.cek_apel.ui.article

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import bangkit.mobiledev.cek_apel.databinding.ActivityDetailArticleBinding
import bangkit.mobiledev.cek_apel.utils.createCustomTempFile
import java.io.File
import java.io.FileOutputStream

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val toolbar = binding.topAppBar
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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

            // Display handling steps without numbering
            val handlingStepsText = articleHandling.joinToString("\n")
            tvHandlingSteps.text = handlingStepsText
        }

        // Setup Share button click listener
        binding.btnShare.setOnClickListener {
            shareArticleWithImage(articleName, articleDescription, articleHandling, articleImageUrl)
        }
    }

    private fun shareArticleWithImage(
        articleName: String,
        articleDescription: String,
        articleHandling: ArrayList<String>,
        articleImageUrl: String
    ) {
        // Menggabungkan langkah penanganan dengan spasi
        val handlingText = articleHandling.joinToString(" ")  // Tidak ada baris baru, hanya spasi
        val shareText = """
        ${articleName.trim()}
    
        ${articleDescription.trim()}
    
        ${handlingText.trim()}
    """.trimIndent()

        // Download image and share
        Glide.with(this)
            .asBitmap()
            .load(articleImageUrl)
            .into(object : CustomTarget<Bitmap>() {
                @SuppressLint("QueryPermissionsNeeded")
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        // Create a temporary file to store the image
                        val imageFile = createCustomTempFile(this@DetailArticleActivity)
                        val outputStream = FileOutputStream(imageFile)
                        resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.close()

                        // Get content:// URI using FileProvider
                        val imageUri = androidx.core.content.FileProvider.getUriForFile(
                            this@DetailArticleActivity,
                            "${applicationContext.packageName}.fileprovider",
                            imageFile
                        )

                        // Create share intent with multiple extras
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "image/*"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            putExtra(Intent.EXTRA_STREAM, imageUri)

                            // Explicitly grant read permission to all apps that can handle the intent
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        // Create a chooser intent with the share intent
                        val chooserIntent = Intent.createChooser(shareIntent, "Share article via")

                        // Optional: Explicitly grant URI permissions to all target apps
                        val resInfoList = packageManager.queryIntentActivities(chooserIntent, 0)
                        for (resolveInfo in resInfoList) {
                            val packageName = resolveInfo.activityInfo.packageName
                            grantUriPermission(
                                packageName,
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        }

                        startActivity(chooserIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Fallback to text-only sharing if image fails
                        val textShareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        startActivity(Intent.createChooser(textShareIntent, "Share article via"))
                    }
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                    // Handle image loading cancellation if needed
                }

                override fun onLoadFailed(errorDrawable: android.graphics.drawable.Drawable?) {
                    // Fallback to text-only sharing if image loading fails
                    val textShareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    startActivity(Intent.createChooser(textShareIntent, "Share article via"))
                }
            })
    }

}