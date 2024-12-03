package bangkit.mobiledev.cek_apel.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import bangkit.mobiledev.cek_apel.data.response.DataItem
import bangkit.mobiledev.cek_apel.databinding.ItemArticleBinding

class ArticleAdapter(private val onItemClick: (DataItem) -> Unit) :
    ListAdapter<DataItem, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ArticleViewHolder(
        private val binding: ItemArticleBinding,
        private val onItemClick: (DataItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: DataItem) {
            Log.d("ArticleAdapter", "Binding article: $article")
            binding.apply {
                tvArticleTitle.text = article.nama
                tvArticleDescription.text = article.deskripsi

                Glide.with(itemView.context)
                    .load(article.imageUrl)
                    .into(ivArticle)

                root.setOnClickListener { onItemClick(article) }
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}