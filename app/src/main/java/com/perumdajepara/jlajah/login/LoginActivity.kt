package com.perumdajepara.jlajah.login

import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.Gravity
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.main.MainActivity
import com.perumdajepara.jlajah.model.LoginModel
import com.perumdajepara.jlajah.signup.SignupActivity
import com.perumdajepara.jlajah.util.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert


class LoginActivity : AppCompatActivity(), LoginView {

    private val loginPresenter = LoginPresenter()
    private lateinit var alertDialog: DialogInterface
    private lateinit var pbLupaPass: ProgressBar
    private lateinit var edtEmail: AutoCompleteTextView
    private lateinit var edtPassBaru: AutoCompleteTextView
    private lateinit var edtUlangiPass: AutoCompleteTextView
    private lateinit var submitBtn: TextView
    private lateinit var alertLoading: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onAttachView()
    }

    override fun onAttachView() {
        loginPresenter.onAttach(this)
        val rootView = window.decorView.rootView

        tv_login_daftar_btn.onClick {
            startActivity<SignupActivity>()
        }

        btn_login_masuk.onClick {
            if (validationForm()) loginPresenter.login(
                edt_login_email.text.toString(),
                edt_login_password.text.toString(),
                this@LoginActivity
            )

            // sembunyikan keyboard
            hideKeyboard(this@LoginActivity, rootView)
        }

        tv_login_lupa_password.onClick {
            alertLupaPassword()
        }
    }

    private fun alertLupaPassword() {
        alertDialog = alert {
            isCancelable = false
            title = getString(R.string.lupa_password)
            customView {
                verticalLayout {
                    padding = dip(16)
                    pbLupaPass = horizontalProgressBar {
                        isIndeterminate = true
                        hilang()
                    }
                    edtEmail = autoCompleteTextView {
                        hintResource = R.string.email
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    edtPassBaru = autoCompleteTextView {
                        hintResource = R.string.password_baru
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }
                    edtUlangiPass = autoCompleteTextView {
                        hintResource = R.string.ulangi_password
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }

                    verticalLayout {
                        orientation = LinearLayout.HORIZONTAL

                        submitBtn = textView {
                            textResource = R.string.submit
                            textColor = ContextCompat.getColor(ctx, R.color.accent)
                            typeface = Typeface.DEFAULT_BOLD
                            onClick {
                                hideKeyboard(ctx, rootView)
                                if (validationFormLupaPass()) {
                                    loginPresenter.forgetPassword(
                                        edtEmail.text.toString(),
                                        edtPassBaru.text.toString(),
                                        context
                                    )
                                }
                            }
                        }

                        textView {
                            allCaps = true
                            textResource = R.string.batal
                            textColor = ContextCompat.getColor(ctx, R.color.accent)
                            typeface = Typeface.DEFAULT_BOLD
                            onClick {
                                alertDialog.dismiss()
                            }
                        }.lparams {
                            leftMargin = dip(32)
                        }

                    }.lparams {
                        gravity = Gravity.END
                        topMargin = dip(16)
                        bottomMargin = dip(16)
                    }
                }
            }
        }.show()
    }

    private fun validationFormLupaPass(): Boolean {
        var valid = true

        // cek form kosong
        if (edtEmail.text.isEmpty()) {
            edtEmail.error = getString(R.string.email_kosong)
            valid = false
        }
        if (edtPassBaru.text.isEmpty()) {
            edtPassBaru.error = getString(R.string.password_kosong)
            valid = false
        }

        // cek format email
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
            edtEmail.error = getString(R.string.email_tidak_benar)
            valid = false
        }

        // cek format password
        if (edtPassBaru.text.length < 6) {
            edtPassBaru.error = getString(R.string.password_length)
            valid = false
        }
        if (edtPassBaru.text.toString() != edtUlangiPass.text.toString()) {
            edtUlangiPass.error = getString(R.string.password_tidak_sama)
            valid = false
        }

        return valid
    }

    private fun validationForm(): Boolean {
        var valid = true

        // cek form kosong
        if (edt_login_email.text.isEmpty()) {
            edt_login_email.error = getString(com.perumdajepara.jlajah.R.string.email_kosong)
            valid = false
        }
        if (edt_login_password.text.isEmpty()) {
            edt_login_password.error = getString(com.perumdajepara.jlajah.R.string.password_kosong)
            valid = false
        }

        // cek format email
        if (!Patterns.EMAIL_ADDRESS.matcher(edt_login_email.text.toString()).matches()) {
            edt_login_email.error = getString(com.perumdajepara.jlajah.R.string.email_tidak_benar)
            valid = false
        }

        return valid
    }

    override fun showLoading() {
        alertLoading()
    }

    override fun hideLoading() {
        alertLoading.dismiss()
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

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun showLupaPassLoading() {
        pbLupaPass.terlihat()
    }

    override fun hideLupaPassLoading() {
        pbLupaPass.hilang()
    }

    override fun submitBtnIsEnabled(b: Boolean) {
        submitBtn.isEnabled = b
    }

    override fun suksesLogin(it: LoginModel) {
        // simpan data user di sharedpreferences
        getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE).edit().apply {
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

    override fun suksesForgetPass() {
        showAlert(this, getString(R.string.lupa_pass_cek_email))
        alertDialog.dismiss()
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
