package uts.c14230225.kelas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BahanAdapter(
    private val data: MutableList<String>,
    private val onItemDoubleClick: (Int, String) -> Unit,
    private val onAddToCart: (Int, String) -> Unit  // Tambah parameter baru
) : RecyclerView.Adapter<BahanAdapter.BahanViewHolder>() {

    inner class BahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvKategori: TextView = itemView.findViewById(R.id.tvKategori)
        val tvGambar: TextView = itemView.findViewById(R.id.tvGambar)
        val btnAddToCart: ImageButton = itemView.findViewById(R.id.btnAddToCart)

        private var lastClickTime = 0L

        init {
            itemView.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < 300) {
                    // Double click detected
                    onItemDoubleClick(adapterPosition, data[adapterPosition])
                }
                lastClickTime = currentTime
            }

            // Handle click tombol cart
            btnAddToCart.setOnClickListener {
                onAddToCart(adapterPosition, data[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BahanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bahan, parent, false)
        return BahanViewHolder(view)
    }

    override fun onBindViewHolder(holder: BahanViewHolder, position: Int) {
        val item = data[position].split('-')
        holder.tvNama.text = item.getOrNull(0) ?: ""
        holder.tvKategori.text = "Kategori: ${item.getOrNull(1) ?: ""}"
        holder.tvGambar.text = "URL: ${item.getOrNull(2) ?: ""}"
    }

    override fun getItemCount(): Int = data.size
}