package com.perumdajepara.jlajah.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.kategorilokasi.KategoriLokasiActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val kategoriData = mutableListOf<KategoriModel>()
        for (i in 1..10) {
            kategoriData.add(KategoriModel(R.drawable.ic_launcher_background, "Testing"))
        }

        val adapterKategori = KategoriAdapter(kategoriData) {
            startActivity<KategoriLokasiActivity>()
        }

        rv_kategori.apply {
            adapter = adapterKategori
            layoutManager = GridLayoutManager(context, 4)
        }

        val lokasiPopulerData = mutableListOf<LokasiPopulerModel>()
        for (i in 1..10) {
            lokasiPopulerData.add(LokasiPopulerModel(R.drawable.ic_launcher_background, "Testing", "Testing alamat",
                4.0F, 4.0))
        }

        val adapterLokasiPopuler = LokasiPopulerAdapter(lokasiPopulerData)
        rv_lokasi_populer.apply {
            adapter = adapterLokasiPopuler
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        }
    }
}
