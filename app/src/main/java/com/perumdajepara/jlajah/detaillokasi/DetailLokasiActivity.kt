package com.perumdajepara.jlajah.detaillokasi

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.DetailLokasiModel
import com.perumdajepara.jlajah.model.data.LokasiImage
import com.perumdajepara.jlajah.ulasan.UlasanActivity
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.hilang
import com.perumdajepara.jlajah.util.showAlert
import com.perumdajepara.jlajah.util.terlihat
import kotlinx.android.synthetic.main.activity_detail_lokasi.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class DetailLokasiActivity : AppCompatActivity(), DetailLokasiView {

    private val detailLokasiPresenter = DetailLokasiPresenter()
    private val imageData: MutableList<LokasiImage> = mutableListOf()
    private lateinit var imageAdapter: ImageSliderAdapter
    private lateinit var alertLoading: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lokasi)
        onAttachView()
    }

    override fun onAttachView() {
        detailLokasiPresenter.onAttach(this)

        setSupportActionBar(toolbar_detail_lokasi)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        val idLokasi = intent.getIntExtra(ConstantVariable.id, 1)
        val codeLang = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
            .getString(ConstantVariable.myLang, "in")

        detailLokasiPresenter.getDetailLokasi(this, codeLang as String, idLokasi)

        btn_tulis_ulasan.onClick {
            startActivity<UlasanActivity>()
        }

        imageAdapter = ImageSliderAdapter(imageData)
        pager_detail_lokasi.adapter = imageAdapter

        val density = resources.displayMetrics.density

        pageindicator_image_lokasi.apply {
            setViewPager(pager_detail_lokasi)
            radius = 5 * density
        }
    }

    override fun onDetachView() {
        detailLokasiPresenter.onDetach()
        detailLokasiPresenter.disposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun showLoading() {
        pb_lokasi_image.terlihat()
        alertLoading()
    }

    override fun hideLoading() {
        pb_lokasi_image.hilang()
        alertLoading.dismiss()
    }

    override fun showData(it: DetailLokasiModel) {
        // set image
        imageData.clear()
        imageData.addAll(it.image)
        imageAdapter.notifyDataSetChanged()

        // set data lokasi
        val data = it.data[0]
        tv_nama_lokasi.text = data.locationName
        tv_alamat_lokasi.text = data.alamat
        rating_star.rating = data.ratingStar.toFloat()
        tv_rating_score.text = data.ratingScore.toFloat().toString()
        tv_desc_lokasi.text = data.desc

        // set data kontak
        tv_hubungi_kami.onClick {
            val waIntent = Intent()
            val apiWa = "https://api.whatsapp.com/send?phone=${data.noTelp}"
            waIntent.apply {
                action = Intent.ACTION_VIEW
                setData(Uri.parse(apiWa))
            }
            startActivity(waIntent)
        }

        tv_email_kami.onClick {
            email(data.email)
        }

        tv_menuju_lokasi.onClick {
            val mapIntent = Intent()
            val uri = Uri.parse("geo:0,0?q=${data.latitude},${data.longitude}(${data.locationName})")
            mapIntent.apply {
                action = Intent.ACTION_VIEW
                setData(uri)
            }
            startActivity(mapIntent)
        }
    }

    private fun alertLoading() {
        alertLoading = alert {
            isCancelable = false
            customView {
                verticalLayout {
                    horizontalProgressBar {
                        isIndeterminate = true
                        padding = dip(32)
                    }
                }
            }
        }.show()
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
