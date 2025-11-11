package uts.c14230225.kelas

import android.app.AlertDialog
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import uts.c14230225.kelas.databinding.FragmentBahanBinding
import uts.c14230225.kelas.databinding.FragmentProfileBinding

class BahanFragment : Fragment() {

    private var binding: FragmentBahanBinding? = null
    var data = mutableListOf<String>()

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

        data.addAll(listOf("Gula-Bumbu", "Garam-Bumbu", "Merica-Bumbu", "Ayam-Daging", "Sapi-Daging"))

        val lvAdapter : ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data
        )

        binding?.addCart?.setOnClickListener {
            var nama = binding!!.namabahan.text.toString()
            var kategori = binding!!.kategoribahan.text.toString()

            if(nama == "") {
                // toast
            } else if(kategori == "") {
                // toast
            } else {
                data.add("$nama-$kategori")
                lvAdapter.notifyDataSetChanged()
                binding!!.namabahan.setText("")
                binding!!.kategoribahan.setText("")
            }
        }

        binding?.lvbahan?.adapter = lvAdapter

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = binding!!.lvbahan.pointToPosition(e.x.toInt(), e.y.toInt())

                    if (position != ListView.INVALID_POSITION) {
                        val selectedItem = data[position]
                        showActionDialog(position, selectedItem, data, lvAdapter)
                    }
                    return true
                }
            }
        )

        binding!!.lvbahan.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

        return binding!!.root
    }

    private fun showActionDialog(
        position: Int,
        selectedItem: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("ITEM $selectedItem")
        builder.setMessage("Pilih tindakan yang ingin dilakukan:")

        builder.setPositiveButton("Update"){ _,_ ->
            showUpdateDialog(position, selectedItem, data, adapter)
        }

        builder.setNegativeButton("Hapus"){_,_ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
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
        adapter: ArrayAdapter<String>
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

        layout.addView(tvOld)
        layout.addView(etNewNama)
        layout.addView(etNewKategori)

        builder.setView(layout)

        builder.setPositiveButton("Simpan"){ dialog, which ->
            val newValue = etNewNama.text.toString().trim() + "-" + etNewKategori.text.toString().trim()
            if (newValue.isNotEmpty()){
                data[position] = newValue
                adapter.notifyDataSetChanged()
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