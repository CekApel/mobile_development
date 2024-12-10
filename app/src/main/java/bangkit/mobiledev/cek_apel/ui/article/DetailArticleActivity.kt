package bangkit.mobiledev.cek_apel.ui.article

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import bangkit.mobiledev.cek_apel.databinding.ActivityDetailArticleBinding
import java.io.File
import java.io.FileOutputStream

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.progressBar.visibility = View.VISIBLE

        binding.btnShare.visibility = View.GONE

        val toolbar = binding.topAppBar
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val articleName = intent.getStringExtra("ARTICLE_NAME") ?: ""
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION") ?: ""
        val articleHandling = intent.getStringArrayListExtra("ARTICLE_HANDLING") ?: arrayListOf("Tidak ada informasi penanganan")
        val articleImageUrl = intent.getStringExtra("ARTICLE_IMAGE_URL") ?: ""

        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                tvArticleTitle.text = articleName
                tvArticleDescription.text = articleDescription

                Glide.with(this@DetailArticleActivity)
                    .load(articleImageUrl)
                    .into(ivArticle)

                val handlingStepsText = articleHandling.joinToString("\n")
                tvHandlingSteps.text = handlingStepsText

                progressBar.visibility = View.GONE

                btnShare.visibility = View.VISIBLE
            }
        }, 1000)

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
        val handlingText = articleHandling.joinToString(" ")
        val shareText = """
        ${articleName.trim()}
    
        ${articleDescription.trim()}
    
        ${handlingText.trim()}
    """.trimIndent()

        Glide.with(this)
            .asBitmap()
            .load(articleImageUrl)
            .into(object : CustomTarget<Bitmap>() {
                @SuppressLint("QueryPermissionsNeeded")
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg")
                        val outputStream = FileOutputStream(imageFile)
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()

                        val imageUri = FileProvider.getUriForFile(
                            this@DetailArticleActivity,
                            "${applicationContext.packageName}.fileprovider",
                            imageFile
                        )

                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "image/*"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            putExtra(Intent.EXTRA_STREAM, imageUri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        val chooserIntent = Intent.createChooser(shareIntent, "Share article via")

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
                        val textShareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        startActivity(Intent.createChooser(textShareIntent, "Share article via"))
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    val textShareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    startActivity(Intent.createChooser(textShareIntent, "Share article via"))
                }
            })
    }
}