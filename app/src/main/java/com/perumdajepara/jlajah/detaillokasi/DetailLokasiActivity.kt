package com.perumdajepara.jlajah.detaillokasi

import android.app.Activity
import android.content.ActivityNotFoundException
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
import kotlinx.android.synthetic.main.activity_detail_lokasi.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class DetailLokasiActivity : AppCompatActivity(), DetailLokasiView {

    private val detailLokasiPresenter = DetailLokasiPresenter()
    private val imageData: MutableList<LokasiImage> = mutableListOf()
    private lateinit var imageAdapter: ImageSliderAdapter
    private lateinit var alertLoading: DialogInterface

    private var idLokasi = 0
    private var count = 0

    private var icon: String = ""
    private val ulasanCode = 104
    private var ulasanDiEdit = false

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

         idLokasi = intent.getIntExtra(ConstantVariable.id, 0)

        detailLokasiPresenter.getDetailLokasi(this, getMyLang(this), idLokasi)
        detailLokasiPresenter.getReviewByUser(this, idLokasi, getToken(this))
        detailLokasiPresenter.cekBookmark(this, getToken(this), idLokasi)

        btn_tulis_ulasan.onClick {
            val intent = Intent(applicationContext, UlasanActivity::class.java)
            intent.apply {
                putExtra(ConstantVariable.id, idLokasi)
                putExtra(ConstantVariable.review, "")
                putExtra(ConstantVariable.rating, 1)
            }
            startActivityForResult(intent, ulasanCode)
        }

        tv_edit_ulasan.onClick {
            val intent = Intent(applicationContext, UlasanActivity::class.java)
            intent.apply {
                putExtra(ConstantVariable.id, idLokasi)
                putExtra(ConstantVariable.review, tv_deskripsi_review.text.toString())
                putExtra(ConstantVariable.rating, rating_review.rating.toInt())
            }
            startActivityForResult(intent, ulasanCode)
        }

        tv_lihat_ulasan.onClick {
            startActivity<AllReviewActivity>(
                ConstantVariable.id to idLokasi,
                ConstantVariable.fullName to tv_nama_lokasi.text.toString()
            )
        }

        tv_kunjungi_kami.onClick {
            try {
                val intent = packageManager.getLaunchIntentForPackage("com.perumdajepara.tesjlajah")
                intent?.putExtra("testing", "mantab")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val market = Uri.parse("market://details?id=com.perusdajepara.jeparaadvertiser")
                val intent = Intent(Intent.ACTION_VIEW, market)
                startActivity(intent)
            }
        }

        swipe_detail_lokasi.setOnRefreshListener {
            detailLokasiPresenter.getDetailLokasi(this, getMyLang(this), idLokasi)
            detailLokasiPresenter.getReviewByUser(this, idLokasi, getToken(this))
        }

        imageAdapter = ImageSliderAdapter(imageData)
        pager_detail_lokasi.adapter = imageAdapter

        val density = resources.displayMetrics.density

        pageindicator_image_lokasi.apply {
            setViewPager(pager_detail_lokasi)
            radius = 5 * density
        }

        btn_bookmark.onClick {
            when (count) {
                1 -> {
                    detailLokasiPresenter.deleteBookmark(this@DetailLokasiActivity, idLokasi, getToken(this@DetailLokasiActivity))
                }
                else -> {
                    detailLokasiPresenter.addBookmark(this@DetailLokasiActivity, getToken(this@DetailLokasiActivity), idLokasi)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ulasanCode -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ulasanDiEdit = data.getBooleanExtra(ConstantVariable.status, false)

                    if (ulasanDiEdit) {
                        cv_ulasan.hilang()
                        btn_tulis_ulasan.hilang()
                        detailLokasiPresenter.getReviewByUser(this, idLokasi, getToken(this))
                    }
                }
            }
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
        btn_bookmark.hilang()
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

    override fun suksesBookmark() {
        toast(getString(R.string.lokasi_ditambahkan_bookmark))
        detailLokasiPresenter.cekBookmark(this, getToken(this), idLokasi)
    }

    override fun suksesDelete() {
        toast(R.string.lokasi_dihapus_bookmark)
        detailLokasiPresenter.cekBookmark(this, getToken(this), idLokasi)
    }

    override fun cekBookmark(count: Int) {
        this.count = count
        if (count == 1) {
            btn_bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
        } else {
            btn_bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
        }
        btn_bookmark.terlihat()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
