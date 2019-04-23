package com.perumdajepara.jlajah.allreview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.data.Review
import kotlinx.android.synthetic.main.item_all_review.view.*

class ReviewAdapter(private val data: List<Review>): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_all_review, parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bindItem(data)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        @SuppressLint("SetTextI18n")
        fun bindItem(data: Review) {
            v.tv_username.text = "${data.fullName} (${data.username})"
            v.rating_review.rating = data.ratingStar.toFloat()
            v.tv_waktu_review.text = data.updatedAt
            v.tv_deskripsi_review.text = data.review
        }
    }
}