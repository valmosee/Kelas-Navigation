package uts.c14230225.kelas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val data: MutableList<Bahan>,
    private val onRemoveFromCart: (Int, Bahan) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaCart)
        val tvKategori: TextView = itemView.findViewById(R.id.tvKategoriCart)
        val tvGambar: TextView = itemView.findViewById(R.id.tvGambarCart)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemoveFromCart)

        init {
            btnRemove.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onRemoveFromCart(adapterPosition, data[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val bahan = data[position]
        holder.tvNama.text = bahan.namaBahan
        holder.tvKategori.text = "Kategori: ${bahan.katBahan}"
        holder.tvGambar.text = "URL: ${bahan.gbrBahan}"
    }

    override fun getItemCount(): Int = data.size
}