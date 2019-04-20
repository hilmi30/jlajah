package com.perumdajepara.jlajah.account


import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.login.LoginActivity
import com.perumdajepara.jlajah.main.MainActivity
import com.perumdajepara.jlajah.model.EditProfileModel
import com.perumdajepara.jlajah.util.*
import com.perumdajepara.jlajah.util.ConstantVariable.Companion.lang
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class AccountFragment : Fragment(), AccountView {

    private lateinit var rbIndo: RadioButton
    private lateinit var rbEng: RadioButton
    private lateinit var rbPria: RadioButton
    private lateinit var rbWanita: RadioButton
    private lateinit var edtPassLama: EditText
    private lateinit var edtPassBaru: EditText
    private lateinit var edtUlangiPass: EditText
    private lateinit var pbGantiPass: ProgressBar
    private lateinit var alertDialog: DialogInterface
    private lateinit var alertLoading: DialogInterface
    private lateinit var rootView: View
    private lateinit var submitBtn: TextView
    private var genderId: Int = 1
    // set presenter
    private val accountPresenter = AccountPresenter()
    // sharepref
    private lateinit var userPref: SharedPreferences
    private lateinit var userToken: String
    private var userId: Int = 0
    private var lang: String = "in"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_account, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onAttachView()
    }

    override fun onAttachView() {
        accountPresenter.onAttach(this)

        (activity as AppCompatActivity).setSupportActionBar(toolbar_account)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        // panggil user pref
        userPref = ctx.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        // get item userpref
        userToken = userPref.getString(ConstantVariable.accessToken, "") as String
        userId = userPref.getInt(ConstantVariable.id, 0)
        lang = userPref.getString(ConstantVariable.myLang, "in") as String

        // cek bahasa default
        tv_bahasa.text = if (lang == "in") getString(R.string.indonesia) else getString(R.string.english)

        tv_logout.onClick {
            alert {
                message = getString(R.string.yakin_keluar)
                positiveButton(getString(R.string.ya)) {
                    // hapus data user di sharepreferences
                    userPref.edit().clear().apply()

                    startActivity<LoginActivity>()
                    activity?.finish()
                }
                negativeButton(getString(R.string.tidak)) {
                    it.dismiss()
                }
            }.show()
        }

        setAccountData()

        lyt_bahasa.onClick {
            bahasaAlert()
        }

        tv_ganti_password.onClick {
            passwordAlert(userToken)
        }

        tv_gender.onClick {
            genderAlert()
        }
    }

    private fun setAccountData() {
        // non editable edittext
        tv_nama_user.text = userPref.getString(ConstantVariable.fullName, "")
        tv_email.text = userPref.getString(ConstantVariable.email, "")
        tv_username.text = userPref.getString(ConstantVariable.username, "")
        tv_username2.text = userPref.getString(ConstantVariable.username, "")
        // editable edittext
        tv_nama_user2.setText(userPref.getString(ConstantVariable.fullName, ""))
        tv_notelp.setText(userPref.getString(ConstantVariable.nomerHp, ""))
        tv_gender.setText(userPref.getString(ConstantVariable.gender, ""))
        genderId = userPref.getInt(ConstantVariable.genderId, 1)
    }

    private fun genderAlert() {
        alert {
            isCancelable = false
            customView {
                verticalLayout {
                    padding = dip(16)
                    radioGroup {
                        orientation = RadioGroup.HORIZONTAL
                        rbPria = radioButton {
                            text = getString(com.perumdajepara.jlajah.R.string.pria)
                            tag = 1
                        }
                        rbWanita = radioButton {
                            text = getString(com.perumdajepara.jlajah.R.string.wanita)
                            tag = 2
                        }.lparams {
                            leftMargin = dip(16)
                        }

                        when (genderId) {
                            1 -> check(rbPria.id)
                            2 -> check(rbWanita.id)
                        }
                    }
                }
            }
            positiveButton(getString(com.perumdajepara.jlajah.R.string.pilih)) {
                tv_gender.setText(if (rbPria.isChecked) getString(R.string.pria) else getString(R.string.wanita))
                genderId = if (rbPria.isChecked) rbPria.tag.toString().toInt() else rbWanita.tag.toString().toInt()
            }
            negativeButton(getString(R.string.batal)) {
                it.dismiss()
            }
        }.show()
    }

    private fun passwordAlert(userToken: String) {
        alertDialog = alert {
            isCancelable = false
            title = getString(R.string.ganti_password)
            customView {
                verticalLayout {
                    padding = dip(16)
                    pbGantiPass = horizontalProgressBar {
                        isIndeterminate = true
                        hilang()
                    }
                    edtPassLama = editText {
                        hintResource = R.string.password_lama
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }
                    edtPassBaru = editText {
                        hintResource = R.string.password_baru
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }
                    edtUlangiPass = editText {
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
                                if (validationForm()) {
                                    accountPresenter.resetPassword(
                                        userToken,
                                        edtPassLama.text.toString(),
                                        edtPassBaru.text.toString(),
                                        context
                                    )
                                }
                            }
                        }

                        textView {
                            allCaps = true
                            text = getString(R.string.batal)
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

    private fun bahasaAlert() {
        alert {
            isCancelable = false
            customView {
                verticalLayout {
                    padding = dip(16)
                    radioGroup {
                        rbIndo = radioButton {
                            text = getString(R.string.indonesia)
                            tag = "in"
                        }.lparams {
                            bottomMargin = dip(16)
                        }
                        rbEng = radioButton {
                            text = getString(R.string.english)
                            tag = "en"
                        }

                        check(if (lang == rbIndo.tag) rbIndo.id else rbEng.id)
                    }
                }
            }
            positiveButton(getString(R.string.pilih)) {
                val tag: String
                val bahasa: String

                if (rbIndo.isChecked) {
                    tag = rbIndo.tag.toString()
                    bahasa = rbIndo.text.toString()
                } else {
                    tag = rbEng.tag.toString()
                    bahasa = rbEng.text.toString()
                }

                // ganti bahasa
                setLocale(ctx, tag)
                tv_bahasa.text = bahasa

                toast(getString(R.string.bahasa_diubah))

                // lempar ke main activity
                startActivity<MainActivity>()
                activity?.finish()
            }
            negativeButton(getString(R.string.batal)) {
                it.dismiss()
            }
        }.show()
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
        pbGantiPass.terlihat()
    }

    override fun suksesResetPass() {
        showAlert(ctx, getString(R.string.password_diubah))
        alertDialog.dismiss()
    }

    override fun hideLoading() {
        pbGantiPass.hilang()
    }

    override fun error(msg: String?) {
        showAlert(ctx, msg.toString())
    }

    override fun submitResetBtnIsEnabled(b: Boolean) {
        submitBtn.isEnabled = b
    }

    override fun suksesGantiProfil(it: EditProfileModel) {
        userPref.edit().apply {
            putString(ConstantVariable.fullName, it.data.namaLengkap)
            putInt(ConstantVariable.genderId, it.data.gender.id)
            putString(ConstantVariable.gender, it.data.gender.gender)
            putString(ConstantVariable.nomerHp, it.data.nomerHp)
            apply()
        }

        showAlert(ctx, getString(R.string.profil_diubah))
        setAccountData()
    }

    override fun showProfilLoading() {
        alertLoading()
    }

    override fun hideProfilLoading() {
        alertLoading.dismiss()
    }

    private fun validationForm(): Boolean {
        var valid = true

        // cek form kosong
        if (edtPassLama.text.isEmpty()) {
            edtPassLama.error = getString(R.string.password_lama_kosong)
            valid = false
        }
        if (edtPassBaru.text.isEmpty()) {
            edtPassBaru.error = getString(R.string.password_baru_kosong)
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


    override fun onDetachView() {
        accountPresenter.onDetach()
        accountPresenter.disposable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDetachView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.simpan -> {
                accountPresenter.editProfile(
                    userToken,
                    userId,
                    tv_nama_user2.text.toString(),
                    genderId,
                    tv_notelp.text.toString(),
                    ctx
                )
                hideKeyboard(ctx, rootView)
            }
        }
        return true
    }
}
