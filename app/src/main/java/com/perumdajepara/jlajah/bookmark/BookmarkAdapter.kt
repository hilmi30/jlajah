package com.perumdajepara.jlajah.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.LokasiFavoritModel
import com.perumdajepara.jlajah.model.data.Lokasi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_lokasi_by_kategori.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class BookmarkAdapter(private val data: List<LokasiFavoritModel>, private val listener: (LokasiFavoritModel) -> Unit)
    : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lokasi_by_kategori, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data, listener)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: LokasiFavoritModel, listener: (LokasiFavoritModel) -> Unit) {

            Picasso.get().load(data.icon).placeholder(R.drawable.kategori_placeholder).error(R.drawable.kategori_placeholder)
                .into(v.img_lokasi_kategori)

            v.tv_nama_lokasi_kategori.text = data.locationName
            v.tv_alamat_lokasi_kategori.text = data.alamat

            itemView.onClick {
                listener(data)
            }
        }
    }
}