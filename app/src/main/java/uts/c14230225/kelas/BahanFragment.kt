package uts.c14230225.kelas

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import uts.c14230225.kelas.databinding.FragmentBahanBinding

class BahanFragment : Fragment() {

    private var binding: FragmentBahanBinding? = null
    var data = mutableListOf<String>()
    private lateinit var adapter: BahanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBahanBinding.inflate(
            inflater, container, false
        )

        val dataBahan = resources.getStringArray(R.array.data_bahan)
        data.addAll(dataBahan)

        // Setup RecyclerView
        adapter = BahanAdapter(data) { position, selectedItem ->
            showActionDialog(position, selectedItem, data, adapter)
        }

        binding?.rvbahan?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvbahan?.adapter = adapter

        binding?.btnAdd?.setOnClickListener {
            var _nama = binding!!.namabahan.text.toString()
            var _kategori = binding!!.kategoribahan.text.toString()
            var _gambar = binding!!.gambarbahan.text.toString()

            if(_nama == "") {
                Toast.makeText(requireContext(), "Nama bahan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if(_kategori == "") {
                Toast.makeText(requireContext(), "Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if(_gambar == "") {
                Toast.makeText(requireContext(), "URL gambar tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                data.add("$_nama-$_kategori-$_gambar")
                adapter.notifyItemInserted(data.size - 1)
                binding!!.namabahan.setText("")
                binding!!.kategoribahan.setText("")
                binding!!.gambarbahan.setText("")
            }
        }

        return binding!!.root
    }

    private fun showActionDialog(
        position: Int,
        selectedItem: String,
        data: MutableList<String>,
        adapter: BahanAdapter
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("ITEM $selectedItem")
        builder.setMessage("Pilih tindakan yang ingin dilakukan:")

        builder.setPositiveButton("Update"){ _,_ ->
            showUpdateDialog(position, selectedItem, data, adapter)
        }

        builder.setNegativeButton("Hapus"){_,_ ->
            data.removeAt(position)
            adapter.notifyItemRemoved(position)
            Toast.makeText(
                requireContext(),
                "Hapus Item $selectedItem",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNeutralButton("Batal"){dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showUpdateDialog(
        position: Int,
        oldValue : String,
        data: MutableList<String>,
        adapter: BahanAdapter
    ){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Data")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val tvOld = TextView(requireContext())
        tvOld.text = "Data lama: $oldValue"
        tvOld.textSize = 16f

        var itemOld = oldValue.split('-')

        val etNewNama = EditText(requireContext())
        etNewNama.hint = "Masukkan Nama Bahan baru"
        etNewNama.setText(itemOld[0])

        val etNewKategori = EditText(requireContext())
        etNewKategori.hint = "Masukkan Kategori baru"
        etNewKategori.setText(itemOld[1])

        val etNewGambar = EditText(requireContext())
        etNewGambar.hint = "Masukkan URL Gambar baru"
        etNewGambar.setText(if(itemOld.size > 2) itemOld[2] else "")

        layout.addView(tvOld)
        layout.addView(etNewNama)
        layout.addView(etNewKategori)
        layout.addView(etNewGambar)

        builder.setView(layout)

        builder.setPositiveButton("Simpan"){ dialog, which ->
            val newValue = etNewNama.text.toString().trim() + "-" +
                    etNewKategori.text.toString().trim() + "-" +
                    etNewGambar.text.toString().trim()

            if (newValue.isNotEmpty()){
                data[position] = newValue
                adapter.notifyItemChanged(position)
                Toast.makeText(
                    requireContext(),
                    "Data diupdate jadi: $newValue",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Data baru tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal"){ dialog, which ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}