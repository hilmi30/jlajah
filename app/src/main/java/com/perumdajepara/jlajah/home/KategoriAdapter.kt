package com.perumdajepara.jlajah.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.data.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_kategori.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class KategoriAdapter(private val data: List<Category>, private val listener: (Category) -> Unit)
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
        fun bindItem(data: Category, listener: (Category) -> Unit) {
            Picasso.get().load(data.icon).placeholder(R.drawable.kategori_placeholder)
                .error(R.drawable.kategori_placeholder).into(v.img_kategori_image)

            v.tv_kategori_title.text = data.nameCategory

            itemView.onClick {
                listener(data)
            }
        }
    }
}