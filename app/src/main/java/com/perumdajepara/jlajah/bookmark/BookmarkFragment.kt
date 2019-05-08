package com.perumdajepara.jlajah.bookmark


import android.content.Intent
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
import com.perumdajepara.jlajah.model.LokasiFavoritModel
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.*
import kotlinx.android.synthetic.main.fragment_bookmark.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.support.v4.*

class BookmarkFragment : Fragment(), BookmarkView {

    private lateinit var database: SQLHelper
    private var favoritData: MutableList<Lokasi> = mutableListOf()
    private lateinit var favorit: List<LokasiFavoritModel>
    private lateinit var adapterLokasi: BookmarkAdapter
    private val detailLokasiCode = 101
    private val bookmarkPresenter = BookmarkPresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onAttachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            detailLokasiCode -> {
                bookmarkPresenter.getBookmarkByUser(context = ctx, token = getToken(ctx), codeLang = getMyLang(ctx))
            }
        }
    }

    override fun showLoading() {
        swipe_bookmark.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_bookmark.isRefreshing = false
    }

    override fun showData(items: List<Lokasi>) {
        favoritData.clear()
        favoritData.addAll(items)
        adapterLokasi.notifyDataSetChanged()
    }

    override fun error(msg: String) {
        alert {
            isCancelable = false
            message = msg
            positiveButton(getString(R.string.coba_lagi)) {
                bookmarkPresenter.getBookmarkByUser(context = ctx, token = getToken(ctx), codeLang = getMyLang(ctx))
            }
            negativeButton(getString(R.string.tutup)) {
                it.dismiss()
            }
        }.show()
    }

    override fun suksesDelete(msg: String) {
        showAlert(ctx, msg)
        bookmarkPresenter.getBookmarkByUser(ctx, getToken(ctx), getMyLang(ctx))
    }

    override fun showDataKosong() {
        tv_bookmark_kosong.terlihat()
    }

    override fun hideDataKosong() {
        tv_bookmark_kosong.hilang()
    }

    override fun onAttachView() {
        bookmarkPresenter.onAttach(this)

        bookmarkPresenter.getBookmarkByUser(ctx, getToken(ctx), getMyLang(ctx))

        swipe_bookmark.setOnRefreshListener {
            bookmarkPresenter.getBookmarkByUser(ctx, getToken(ctx), getMyLang(ctx))
        }

        adapterLokasi = BookmarkAdapter(favoritData) {
            val intent = Intent(context, DetailLokasiActivity::class.java)
            intent.putExtra(ConstantVariable.id, it.id.toInt())
            startActivityForResult(intent, detailLokasiCode)
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
                        bookmarkPresenter.deleteBookmark(ctx, favoritData[position].id.toInt(), getToken(ctx))

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

    override fun onDetachView() {
        bookmarkPresenter.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDetachView()
    }
}
