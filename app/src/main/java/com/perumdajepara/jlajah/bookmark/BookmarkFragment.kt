package com.perumdajepara.jlajah.bookmark


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.perumdajepara.jlajah.R

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

//        val kategoriLokasiData = ArrayList<KategoriLokasiModel>()
//        for (i in 1..5) {
//            kategoriLokasiData.add(KategoriLokasiModel(R.drawable.ic_launcher_background, "Pantai Kartini", "Kabupaten Jepara"))
//        }

//        val adapterKategoriLokasi = LokasiByCategoryAdapter(kategoriLokasiData) {
//            startActivity<DetailLokasiActivity>()
//        }

//        rv_bookmark.apply {
//            adapter = adapterKategoriLokasi
//            layoutManager = LinearLayoutManager(context)
//        }

//        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)
//                    : Boolean = false
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//
//                alert {
//                    title = getString(R.string.hapus_lokasi)
//                    message = getString(R.string.yakin_hapus_lokasi)
//                    positiveButton(getString(R.string.ya)) {
//                        toast("terhapus")
//                    }
//                    negativeButton(getString(R.string.tidak)) {
//                        adapterKategoriLokasi.notifyItemChanged(position)
//                        it.dismiss()
//                    }
//                }.show()
//            }
//
//        }
//
//        val touchHelper = ItemTouchHelper(swipeToDeleteCallback)
//        touchHelper.attachToRecyclerView(rv_bookmark)

    }
}
