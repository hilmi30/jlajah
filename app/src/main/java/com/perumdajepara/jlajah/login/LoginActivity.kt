package com.perumdajepara.jlajah.login

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.main.MainActivity
import com.perumdajepara.jlajah.model.LoginModel
import com.perumdajepara.jlajah.signup.SignupActivity
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.hilang
import com.perumdajepara.jlajah.util.terlihat
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity(), LoginView {

    private val loginPresenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onAttachView()
    }

    override fun onAttachView() {
        loginPresenter.onAttach(this)

        tv_login_daftar_btn.onClick {
            startActivity<SignupActivity>()
        }

        btn_login_masuk.onClick {
            if (validationForm()) loginPresenter.login(
                edt_login_email.text.toString(),
                edt_login_password.text.toString()
            )
        }
    }

    private fun validationForm(): Boolean {
        var valid = true

        // cek form kosong
        if (edt_login_email.text.isEmpty()) {
            edt_login_email.error = getString(R.string.email_kosong)
            valid = false
        }
        if (edt_login_password.text.isEmpty()) {
            edt_login_password.error = getString(R.string.password_kosong)
            valid = false
        }

        // cek format email
        if (!Patterns.EMAIL_ADDRESS.matcher(edt_login_email.text.toString()).matches()) {
            edt_login_email.error = getString(R.string.email_tidak_benar)
            valid = false
        }

        return valid
    }

    override fun showLoading() {
        pb_login.terlihat()
    }

    override fun hideLoading() {
        pb_login.hilang()
    }

    override fun cekKoneksi() {
        showAlert(getString(R.string.cek_koneksi))
    }

    private fun showAlert(msg: String) {
        alert {
            message = msg
            okButton {
                it.dismiss()
            }
        }.show()
    }

    override fun suksesLogin(it: LoginModel) {
        // simpan data user di sharedpreferences
        val userTokenPref = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        userTokenPref.edit().apply {
            putInt(ConstantVariable.id, it.id)
            putString(ConstantVariable.username, it.username)
            putString(ConstantVariable.email, it.email)
            putString(ConstantVariable.accessToken, it.accessToken)
            putString(ConstantVariable.fullName, it.fullName)
            putInt(ConstantVariable.genderId, it.gender.id)
            putString(ConstantVariable.gender, it.gender.gender)
            putString(ConstantVariable.nomerHp, it.nomerHp)
            // status user login menjadi true
            putBoolean(ConstantVariable.status, true)
            apply()

            startActivity<MainActivity>()
            finish()
        }
    }

    override fun usernamePasswordSalah() {
        showAlert(getString(R.string.username_password_salah))
    }

    override fun terjadiKesalahan() {
        showAlert(getString(R.string.terjadi_kesalahan))
    }

    override fun onDetachView() {
        loginPresenter.onDetach()
        loginPresenter.disposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }
}
