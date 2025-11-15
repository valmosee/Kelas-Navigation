package uts.c14230225.kelas

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uts.c14230225.kelas.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var binding: FragmentCartBinding? = null
    lateinit var sp: SharedPreferences
    var cartList: MutableList<Bahan> = mutableListOf()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        sp = requireActivity().getSharedPreferences("datashared", Context.MODE_PRIVATE)

        // Load cart dari SharedPreferences
        loadCart()

        // Setup RecyclerView
        adapter = CartAdapter(cartList) { position, bahan ->
            removeFromCart(position, bahan)
        }

        binding?.rvCart?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvCart?.adapter = adapter

        // Update tampilan
        updateUI()

        // Tombol clear cart
        binding?.btnClearCart?.setOnClickListener {
            showClearCartDialog()
        }

        return binding?.root
    }

    // Load cart dari SharedPreferences
    private fun loadCart() {
        var gson = Gson()
        var isicart = sp.getString("dt_cart", null)
        var type = object : TypeToken<ArrayList<Bahan>>() {}.type

        cartList.clear()
        if (isicart != null) {
            val tempList: MutableList<Bahan> = gson.fromJson(isicart, type)
            cartList.addAll(tempList)
        }
    }

    // Hapus item dari cart
    private fun removeFromCart(position: Int, bahan: Bahan) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus dari Keranjang")
            .setMessage("Yakin ingin menghapus ${bahan.namaBahan} dari keranjang?")
            .setPositiveButton("Hapus") { _, _ ->
                cartList.removeAt(position)
                adapter.notifyItemRemoved(position)

                // Simpan perubahan ke SharedPreferences
                saveCart()

                Toast.makeText(
                    requireContext(),
                    "${bahan.namaBahan} dihapus dari keranjang",
                    Toast.LENGTH_SHORT
                ).show()

                // Update tampilan
                updateUI()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Simpan cart ke SharedPreferences
    private fun saveCart() {
        var gson = Gson()
        sp.edit().putString("dt_cart", gson.toJson(cartList)).apply()
    }

    // Clear semua cart
    private fun showClearCartDialog() {
        if (cartList.isEmpty()) {
            Toast.makeText(requireContext(), "Keranjang sudah kosong", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Kosongkan Keranjang")
            .setMessage("Yakin ingin menghapus semua barang dari keranjang?")
            .setPositiveButton("Ya") { _, _ ->
                cartList.clear()
                adapter.notifyDataSetChanged()

                // Hapus dari SharedPreferences
                sp.edit().remove("dt_cart").apply()

                Toast.makeText(
                    requireContext(),
                    "Keranjang berhasil dikosongkan",
                    Toast.LENGTH_SHORT
                ).show()

                // Update tampilan
                updateUI()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Update UI berdasarkan isi cart
    private fun updateUI() {
        if (cartList.isEmpty()) {
            binding?.rvCart?.visibility = View.GONE
            binding?.tvEmptyCart?.visibility = View.VISIBLE
            binding?.btnClearCart?.isEnabled = false
        } else {
            binding?.rvCart?.visibility = View.VISIBLE
            binding?.tvEmptyCart?.visibility = View.GONE
            binding?.btnClearCart?.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        loadCart()
        adapter.notifyDataSetChanged()
        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}