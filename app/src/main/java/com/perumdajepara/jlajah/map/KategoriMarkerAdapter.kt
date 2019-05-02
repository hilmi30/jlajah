package com.perumdajepara.jlajah.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.data.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_kategori.view.*
import kotlinx.android.synthetic.main.item_kategori_marker.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class KategoriMarkerAdapter(private val data: List<Category>, private val listener: (Category) -> Unit)
    : RecyclerView.Adapter<KategoriMarkerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_marker, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data, listener)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: Category, listener: (Category) -> Unit) {

            v.tv_kategori_marker.text = data.nameCategory

            itemView.onClick {
                listener(data)
            }
        }
    }
}