package com.perumdajepara.jlajah.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import kotlinx.android.synthetic.main.item_kategori.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class KategoriAdapter(private val data: List<KategoriModel>, private val listener: (KategoriModel) -> Unit)
    : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data, listener)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: KategoriModel, listener: (KategoriModel) -> Unit) {
            v.img_kategori_image.setImageResource(data.img)
            v.tv_kategori_title.text = data.title

            itemView.onClick {
                listener(data)
            }
        }
    }
}