package com.perumdajepara.jlajah.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.data.Lokasi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_lokasi_populer.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class LokasiPopulerAdapter(private val data: List<Lokasi>, private val listener: (Lokasi) -> Unit)
    : RecyclerView.Adapter<LokasiPopulerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lokasi_populer, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data, listener)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(
            data: Lokasi,
            listener: (Lokasi) -> Unit
        ) {
            Picasso.get().load(data.icon).error(R.drawable.kategori_placeholder).placeholder(R.drawable.kategori_placeholder)
                .into(v.img_lokasi_image)
            v.tv_nama_lokasi.text = data.locationName
            v.rsb_bintang_lokasi.rating = data.ratingScore.toFloat()
            v.tv_score_lokasi.text = data.ratingScore
            v.tv_alamat_lokasi.text = data.alamat

            itemView.onClick {
                listener(data)
            }
        }
    }
}