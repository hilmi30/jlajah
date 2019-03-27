package com.perumdajepara.jlajah.kategorilokasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import org.jetbrains.anko.sdk27.coroutines.onClick

class KategoriLokasiAdapter(private val data: List<KategoriLokasiModel>, private val listener: (KategoriLokasiModel) -> Unit)
    : RecyclerView.Adapter<KategoriLokasiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_lokasi, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data, listener)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(
            data: KategoriLokasiModel,
            listener: (KategoriLokasiModel) -> Unit
        ) {
            itemView.onClick {
                listener(data)
            }
        }
    }
}