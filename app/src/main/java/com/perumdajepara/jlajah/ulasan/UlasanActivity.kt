package com.perumdajepara.jlajah.ulasan

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.showAlert
import kotlinx.android.synthetic.main.activity_ulasan.*
import org.jetbrains.anko.*

class UlasanActivity : AppCompatActivity(), UlasanView {

    private val ulasanPresenter = UlasanPresenter()

    private lateinit var alertLoading: DialogInterface

    private var idLocation = 1
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ulasan)

        onAttachView()
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

    override fun showLoading() {
        alertLoading()
    }

    override fun hideLoading() {
        alertLoading.dismiss()
    }

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun suksesReview() {
        alert {
            message = getString(R.string.review_ditambah)
            negativeButton(R.string.tutup) {
                it.dismiss()
                finish()
            }
        }.show()
    }

    override fun onAttachView() {
        ulasanPresenter.onAttach(this)

        setSupportActionBar(toolbar_ulasan)
        supportActionBar?.apply {
            title = getString(R.string.tulis_ulasan)
            setDisplayHomeAsUpEnabled(true)
        }

        idLocation = intent.getIntExtra(ConstantVariable.id, 1)
        val review = intent.getStringExtra(ConstantVariable.review)
        val rating = intent.getIntExtra(ConstantVariable.rating, 1)
        val userPref = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        accessToken = userPref.getString(ConstantVariable.accessToken, "") as String

        rating_click_detail_lokasi.stepSize = 1.0F
        rating_click_detail_lokasi.rating = rating.toFloat()
        tv_ulasan.setText(review)
    }

    override fun onDetachView() {
        ulasanPresenter.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ulasan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val rating = rating_click_detail_lokasi.rating.toInt()
        when (item?.itemId) {
            R.id.posting -> ulasanPresenter.addReview(this, idLocation, accessToken, tv_ulasan.text.toString(), rating)
            else -> finish()
        }
        return true
    }
}
