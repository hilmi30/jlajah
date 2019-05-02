package com.perumdajepara.jlajah.detaillokasi

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.allreview.AllReviewActivity
import com.perumdajepara.jlajah.model.DetailLokasiModel
import com.perumdajepara.jlajah.model.LokasiFavoritModel
import com.perumdajepara.jlajah.model.data.LokasiImage
import com.perumdajepara.jlajah.model.data.Review
import com.perumdajepara.jlajah.ulasan.UlasanActivity
import com.perumdajepara.jlajah.util.*
import hinl.kotlin.database.helper.Database
import kotlinx.android.synthetic.main.activity_detail_lokasi.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class DetailLokasiActivity : AppCompatActivity(), DetailLokasiView {

    private val detailLokasiPresenter = DetailLokasiPresenter()
    private val imageData: MutableList<LokasiImage> = mutableListOf()
    private lateinit var imageAdapter: ImageSliderAdapter
    private lateinit var alertLoading: DialogInterface
    private lateinit var menu: Menu
    private lateinit var database: SQLHelper

    private var userId = 0
    private var idLokasi = 0
    private var count = 0
    private lateinit var accessToken: String

    private var icon: String = ""

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

        database = SQLHelper(this)
        database.createTable(LokasiFavoritModel::class)

        idLokasi = intent.getIntExtra(ConstantVariable.id, 0)
        // userpref
        val userPref = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        // item user pref
        val codeLang = userPref.getString(ConstantVariable.myLang, "in")
        userId = userPref.getInt(ConstantVariable.id, 0)
        accessToken = userPref.getString(ConstantVariable.accessToken, "") as String

        detailLokasiPresenter.getDetailLokasi(this, codeLang as String, idLokasi)
        detailLokasiPresenter.getReviewByUser(this, idLokasi, accessToken)

        btn_tulis_ulasan.onClick {
            startActivity<UlasanActivity>(
                ConstantVariable.id to idLokasi,
                ConstantVariable.review to "",
                ConstantVariable.rating to 1
            )
        }

        tv_edit_ulasan.onClick {
            startActivity<UlasanActivity>(
                ConstantVariable.id to idLokasi,
                ConstantVariable.review to tv_deskripsi_review.text.toString(),
                ConstantVariable.rating to rating_review.rating.toInt()
            )
        }

        tv_lihat_ulasan.onClick {
            startActivity<AllReviewActivity>(
                ConstantVariable.id to idLokasi,
                ConstantVariable.fullName to tv_nama_lokasi.text.toString()
            )
        }

        swipe_detail_lokasi.setOnRefreshListener {
            detailLokasiPresenter.getDetailLokasi(this, codeLang, idLokasi)
            detailLokasiPresenter.getReviewByUser(this, idLokasi, accessToken)
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
        swipe_detail_lokasi.isRefreshing = false
    }

    override fun showLoading() {
        pb_lokasi_image.terlihat()
        alertLoading()
        btn_tulis_ulasan.hilang()
        cv_ulasan.hilang()
    }

    override fun hideLoading() {
        pb_lokasi_image.hilang()
        alertLoading.dismiss()
        swipe_detail_lokasi.isRefreshing = false
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
        rating_star.rating = it.ratingScore
        tv_rating_score.text = it.ratingScore.toString()
        tv_desc_lokasi.text = data.desc
        icon = data.icon

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

    override fun showReviewLoading() {
        pb_review.terlihat()
    }

    override fun hideReviewLoading() {
        pb_review.hilang()
    }

    override fun showReview(data: Review) {
        cv_ulasan.terlihat()
        btn_tulis_ulasan.hilang()
        rating_review.rating = data.ratingStar.toFloat()
        tv_deskripsi_review.text = data.review
        tv_waktu_review.text = data.updatedAt
    }

    override fun hideReview() {
        cv_ulasan.hilang()
        btn_tulis_ulasan.terlihat()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_lokasi, menu)

        count = database.count(LokasiFavoritModel::class) {
            eq("id_lokasi", idLokasi)
        }

        if (count == 1)
            menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_black_24dp)
        else
            menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_border_black_24dp)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.bookmark -> {

                if (count == 0) {
                    val favorit = LokasiFavoritModel(
                        locationName = tv_nama_lokasi.text.toString(),
                        alamat = tv_alamat_lokasi.text.toString(),
                        icon = icon,
                        idLokasi = idLokasi
                    )

                    database.insert(favorit)
                    invalidateOptionsMenu()
                    toast(getString(R.string.lokasi_ditambahkan_bookmark))
                } else {
                    val lokasi = database.get(LokasiFavoritModel::class) {
                        eq("id_lokasi", idLokasi)
                    }
                    lokasi?.get(0)?.let { database.delete(it) }
                    invalidateOptionsMenu()
                    toast(getString(R.string.lokasi_dihapus_bookmark))
                }
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRestart() {
        super.onRestart()
        cv_ulasan.hilang()
        btn_tulis_ulasan.hilang()
        detailLokasiPresenter.getReviewByUser(this, idLokasi, accessToken)
    }
}
