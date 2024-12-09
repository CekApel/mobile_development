package bangkit.mobiledev.cek_apel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity
import bangkit.mobiledev.cek_apel.databinding.ItemScanBinding
import com.bumptech.glide.Glide

class ScanHistoryAdapter(
    private val onDeleteClick: (ScanHistoryEntity) -> Unit,
    private val onDetailClick: (ScanHistoryEntity) -> Unit
) : ListAdapter<ScanHistoryEntity, ScanHistoryAdapter.ScanHistoryViewHolder>(ScanHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanHistoryViewHolder {
        val binding = ItemScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ScanHistoryViewHolder(private val binding: ItemScanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scanHistory: ScanHistoryEntity) {
            binding.apply {
                textTitle.text = scanHistory.result
                textScanDate.text = scanHistory.scanDate

                // Assuming you have a method to load image from URI
                // image.loadImageFromUri(scanHistory.imageUri)

                scanHistory.imageUri?.let { uri ->
                    Glide.with(itemView.context)
                        .load(uri)
                        .into(image)
                }

                buttonDelete.setOnClickListener {
                    onDeleteClick(scanHistory)
                }

                buttonDetailHistory.setOnClickListener {
                    onDetailClick(scanHistory)
                }
            }
        }
    }

    private class ScanHistoryDiffCallback : DiffUtil.ItemCallback<ScanHistoryEntity>() {
        override fun areItemsTheSame(oldItem: ScanHistoryEntity, newItem: ScanHistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScanHistoryEntity, newItem: ScanHistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}