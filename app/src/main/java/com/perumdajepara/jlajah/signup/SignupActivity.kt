package com.perumdajepara.jlajah.signup

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.login.LoginActivity
import com.perumdajepara.jlajah.util.hideKeyboard
import com.perumdajepara.jlajah.util.hilang
import com.perumdajepara.jlajah.util.showAlert
import com.perumdajepara.jlajah.util.terlihat
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class SignupActivity : AppCompatActivity(), SignupView {

    private var rbPria: RadioButton? = null
    private var rbWanita: RadioButton? = null
    private var nilaiGender = 1
    private var signupPresenter = SignupPresenter()
    private lateinit var alertLoading: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        onAttachView()
    }

    override fun onAttachView() {
        signupPresenter.onAttach(this)
        val rootView = window.decorView.rootView

        setSupportActionBar(toolbar_signup)
        supportActionBar?.apply {
            title = getString(R.string.daftar)
            setDisplayHomeAsUpEnabled(true)
        }

        tv_signup_gender.onClick {
            alertGender()
        }

        btn_signup_daftar.onClick {
            if (formValidation()) signupPresenter.signup(
                edt_signup_username.text.toString(),
                edt_signup_email.text.toString(),
                edt_signup_password.text.toString(),
                edt_signup_nama.text.toString(),
                edt_signup_notelp.text.toString(),
                nilaiGender,
                this@SignupActivity
            )

            // sembunyikan keyboard
            hideKeyboard(this@SignupActivity, rootView)
        }
    }

    private fun formValidation(): Boolean {

        var valid = true

        // cek form kosong
        if (edt_signup_username.text.isEmpty()) {
            edt_signup_username.error = getString(R.string.username_kosong)
            valid = false
        }
        if (edt_signup_email.text.isEmpty()) {
            edt_signup_email.error = getString(R.string.email_kosong)
            valid = false
        }
        if (edt_signup_password.text.isEmpty()) {
            edt_signup_password.error = getString(R.string.password_kosong)
            valid = false
        }
        if (edt_signup_nama.text.isEmpty()) {
            edt_signup_nama.error = getString(R.string.nama_kosong)
            valid = false
        }
        if (edt_signup_notelp.text.isEmpty()) {
            edt_signup_notelp.error = getString(R.string.notelp_kosong)
            valid = false
        }

        // cek format username
        val regex = "^[\\p{L} .'-]+$"
        if (edt_signup_username.text.contains(" ")) {
            edt_signup_username.error = getString(R.string.username_spasi)
            valid = false
        }
        if (edt_signup_username.text.contains(regex)) {
            edt_signup_username.error = getString(R.string.username_tidak_benar)
            valid = false
        }

        // cek format email
        if (!Patterns.EMAIL_ADDRESS.matcher(edt_signup_email.text.toString()).matches()) {
            edt_signup_email.error = getString(R.string.email_tidak_benar)
            valid = false
        }

        // cek format password
        if (edt_signup_password.text.length < 6) {
            edt_signup_password.error = getString(R.string.password_length)
            valid = false
        }
        if (edt_signup_password.text.toString() != edt_signup_ulangi_password.text.toString()) {
            edt_signup_ulangi_password.error = getString(R.string.password_tidak_sama)
            valid = false
        }

        return valid
    }

    override fun onDetachView() {
        signupPresenter.onDetach()
        signupPresenter.disposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    private fun alertGender() {
        alert {
            isCancelable = false
            customView {
                verticalLayout {
                    padding = dip(16)
                    radioGroup {
                        orientation = RadioGroup.HORIZONTAL
                        rbPria = radioButton {
                            textResource = R.string.pria
                            tag = 1
                        }
                        rbWanita = radioButton {
                            textResource = R.string.wanita
                            tag = 2
                        }.lparams {
                            leftMargin = dip(16)
                        }

                        when (nilaiGender) {
                            1 -> check(rbPria?.id as Int)
                            2 -> check(rbWanita?.id as Int)
                        }
                    }
                }
            }
            positiveButton(R.string.pilih) {
                tv_signup_nilai_gender.text = if (rbPria?.isChecked as Boolean) getString(com.perumdajepara.jlajah.R.string.pria) else getString(
                    com.perumdajepara.jlajah.R.string.wanita)
                nilaiGender = if (rbPria?.isChecked as Boolean) rbPria?.tag.toString().toInt() else rbWanita?.tag.toString().toInt()
            }
            negativeButton(getString(com.perumdajepara.jlajah.R.string.batal)) {
                it.dismiss()
            }
        }.show()
    }

    override fun hideLoading() {
        alertLoading.dismiss()
    }

    override fun suksesRegister() {
        startActivity<LoginActivity>()
        finish()
    }

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun showLoading() {
        alertLoading()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
}
