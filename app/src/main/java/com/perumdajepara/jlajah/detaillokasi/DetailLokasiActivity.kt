package com.perumdajepara.jlajah.detaillokasi

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.ulasan.UlasanActivity
import kotlinx.android.synthetic.main.activity_detail_lokasi.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class DetailLokasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lokasi)

        setSupportActionBar(toolbar_detail_lokasi)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        btn_tulis_ulasan.onClick {
            startActivity<UlasanActivity>()
        }

        val data = ArrayList<String>()
        for (i in 1..5) {
            data.add("testing")
        }

        val imgSliderAdapter = ImageSliderAdapter(data)
        pager_detail_lokasi.adapter = imgSliderAdapter

        val density = resources.displayMetrics.density

        pageindicator_image_lokasi.apply {
            setViewPager(pager_detail_lokasi)
            radius = 5 * density
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_lokasi, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
