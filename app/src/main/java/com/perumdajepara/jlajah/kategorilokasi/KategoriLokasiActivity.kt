package com.perumdajepara.jlajah.kategorilokasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import kotlinx.android.synthetic.main.activity_kategori_lokasi.*
import org.jetbrains.anko.startActivity

class KategoriLokasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_lokasi)

        setSupportActionBar(toolbar_kategori_lokasi)
        supportActionBar?.apply {
            title = "Testing"
            setDisplayHomeAsUpEnabled(true)
        }

        val kategoriLokasiData = ArrayList<KategoriLokasiModel>()
        for (i in 1..10) {
            kategoriLokasiData.add(KategoriLokasiModel(R.drawable.ic_launcher_background, "Pantai Kartini", "Kabupaten Jepara"))
        }

        val adapterKategoriLokasi = KategoriLokasiAdapter(kategoriLokasiData) {
            startActivity<DetailLokasiActivity>()
        }
        rv_kategori_lokasi.apply {
            adapter = adapterKategoriLokasi
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
