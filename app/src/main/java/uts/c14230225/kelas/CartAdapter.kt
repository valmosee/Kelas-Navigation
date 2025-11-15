package uts.c14230225.kelas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val data: MutableList<Bahan>,
    private val onRemoveFromCart: (Int, Bahan) -> Unit,
    private val onMarkAsBought: (Int, Bahan) -> Unit  // Callback untuk tandai sudah dibeli
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbBought: CheckBox = itemView.findViewById(R.id.cbBought)
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

            cbBought.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && adapterPosition != RecyclerView.NO_POSITION) {
                    onMarkAsBought(adapterPosition, data[adapterPosition])
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

        // Reset checkbox state
        holder.cbBought.setOnCheckedChangeListener(null)
        holder.cbBought.isChecked = false

        holder.tvNama.text = bahan.namaBahan
        holder.tvKategori.text = "Kategori: ${bahan.katBahan}"
        holder.tvGambar.text = "URL: ${bahan.gbrBahan}"

        // Set listener kembali
        holder.cbBought.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && holder.adapterPosition != RecyclerView.NO_POSITION) {
                holder.itemView.postDelayed({
                    onMarkAsBought(holder.adapterPosition, data[holder.adapterPosition])
                }, 300) // Delay sedikit untuk animasi checkbox
            }
        }
    }

    override fun getItemCount(): Int = data.size
}