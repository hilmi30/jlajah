package com.perumdajepara.jlajah.bookmark


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import com.perumdajepara.jlajah.kategorilokasi.KategoriLokasiAdapter
import com.perumdajepara.jlajah.kategorilokasi.KategoriLokasiModel
import kotlinx.android.synthetic.main.fragment_bookmark.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class BookmarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val kategoriLokasiData = ArrayList<KategoriLokasiModel>()
        for (i in 1..5) {
            kategoriLokasiData.add(KategoriLokasiModel(R.drawable.ic_launcher_background, "Pantai Kartini", "Kabupaten Jepara"))
        }

        val adapterKategoriLokasi = KategoriLokasiAdapter(kategoriLokasiData) {
            startActivity<DetailLokasiActivity>()
        }

        rv_bookmark.apply {
            adapter = adapterKategoriLokasi
            layoutManager = LinearLayoutManager(context)
        }

        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)
                    : Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                alert {
                    title = getString(R.string.hapus_lokasi)
                    message = getString(R.string.yakin_hapus_lokasi)
                    positiveButton(getString(R.string.ya)) {
                        toast("terhapus")
                    }
                    negativeButton(getString(R.string.tidak)) {
                        adapterKategoriLokasi.notifyItemChanged(position)
                        it.dismiss()
                    }
                }.show()
            }

        }

        val touchHelper = ItemTouchHelper(swipeToDeleteCallback)
        touchHelper.attachToRecyclerView(rv_bookmark)

    }
}
