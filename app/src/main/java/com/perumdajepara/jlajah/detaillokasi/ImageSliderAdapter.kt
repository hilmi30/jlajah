package com.perumdajepara.jlajah.detaillokasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.perumdajepara.jlajah.R

class ImageSliderAdapter(private val imgData: ArrayList<String>): PagerAdapter(){
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun getCount(): Int {
        return imgData.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = LayoutInflater.from(container.context).inflate(R.layout.item_image_slider, container, false)



        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object`as RelativeLayout)
    }
}
