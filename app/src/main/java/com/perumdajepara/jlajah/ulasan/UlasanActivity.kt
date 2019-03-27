package com.perumdajepara.jlajah.ulasan

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import kotlinx.android.synthetic.main.activity_ulasan.*

class UlasanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ulasan)

        setSupportActionBar(toolbar_ulasan)
        supportActionBar?.apply {
            title = getString(R.string.tulis_ulasan)
            setDisplayHomeAsUpEnabled(true)
        }

        rating_click_detail_lokasi.stepSize = 1.0F
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ulasan, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
