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
import com.perumdajepara.jlajah.lokasibycategory.LokasiByCategoryAdapter
import com.perumdajepara.jlajah.model.LokasiFavoritModel
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.*
import kotlinx.android.synthetic.main.fragment_bookmark.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class BookmarkFragment : Fragment() {

    private lateinit var database: SQLHelper
    private var favoritData: MutableList<LokasiFavoritModel> = mutableListOf()
    private lateinit var favorit: List<LokasiFavoritModel>
    private lateinit var adapterLokasi: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        database = SQLHelper(ctx)
        database.createTable(LokasiFavoritModel::class)

        favorit = database.get(LokasiFavoritModel::class) as List<LokasiFavoritModel>

        cekDataKosong()

        favoritData.clear()
        favoritData.addAll(favorit)

        adapterLokasi = BookmarkAdapter(favoritData) {
            startActivity<DetailLokasiActivity>(
                ConstantVariable.id to it.idLokasi
            )
        }

        rv_bookmark.apply {
            adapter = adapterLokasi
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemDecoration(32))
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
                        val item = favoritData[position]
                        database.delete(item)
                        favoritData.removeAt(position)
                        adapterLokasi.notifyDataSetChanged()
                        cekDataKosong()
                    }
                    negativeButton(getString(R.string.tidak)) {
                        adapterLokasi.notifyItemChanged(position)
                        it.dismiss()
                    }
                }.show()
            }

        }

        val touchHelper = ItemTouchHelper(swipeToDeleteCallback)
        touchHelper.attachToRecyclerView(rv_bookmark)
    }

    private fun cekDataKosong() {
        if ((favorit).isEmpty()) tv_bookmark_kosong.terlihat() else tv_bookmark_kosong.hilang()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            val favorit = database.get(LokasiFavoritModel::class)
            favoritData.clear()
            favoritData.addAll(favorit as List<LokasiFavoritModel>)
            adapterLokasi.notifyDataSetChanged()
        }
    }
}
