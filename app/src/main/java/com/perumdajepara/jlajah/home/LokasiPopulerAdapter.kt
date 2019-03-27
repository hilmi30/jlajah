package com.perumdajepara.jlajah.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import kotlinx.android.synthetic.main.item_lokasi_populer.view.*

class LokasiPopulerAdapter(private val data: List<LokasiPopulerModel>): RecyclerView.Adapter<LokasiPopulerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lokasi_populer, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: LokasiPopulerModel) {
            v.img_lokasi_image.setImageResource(data.img)
            v.tv_nama_lokasi.text = data.nama
            v.rsb_bintang_lokasi.rating = data.starRating
            v.tv_score_lokasi.text = data.scoreRating.toString()
        }
    }
}