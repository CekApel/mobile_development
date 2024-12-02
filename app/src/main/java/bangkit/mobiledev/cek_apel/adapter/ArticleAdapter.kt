package bangkit.mobiledev.cek_apel.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bangkit.mobiledev.cek_apel.data.response.DataItem
import bangkit.mobiledev.cek_apel.databinding.ItemArticleBinding
import bangkit.mobiledev.cek_apel.ui.article.DetailArticleActivity

class ArticleAdapter : ListAdapter<DataItem, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: DataItem) {
            binding.articleTitle.text = article.nama
            binding.articleDescription.text = article.deskripsi

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailArticleActivity::class.java).apply {
                    putExtra("ARTICLE_NAME", article.nama)
                    putExtra("ARTICLE_DESCRIPTION", article.deskripsi)
                    putStringArrayListExtra("ARTICLE_HANDLING", ArrayList(article.penangananPenyakit))
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                // Since there's no unique ID, compare all properties
                return oldItem.nama == newItem.nama &&
                        oldItem.deskripsi == newItem.deskripsi &&
                        oldItem.penangananPenyakit == newItem.penangananPenyakit
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                // In this case, areItemsTheSame and areContentsTheSame are the same
                return oldItem.nama == newItem.nama &&
                        oldItem.deskripsi == newItem.deskripsi &&
                        oldItem.penangananPenyakit == newItem.penangananPenyakit
            }
        }
    }
}