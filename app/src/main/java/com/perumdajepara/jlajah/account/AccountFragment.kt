package com.perumdajepara.jlajah.account


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.login.LoginActivity
import com.perumdajepara.jlajah.util.ConstantVariable
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity


class AccountFragment : Fragment() {

    private lateinit var userTokenPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_account)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
        }

        userTokenPref = ctx.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)

        tv_logout.onClick {
            // hapus data user di sharepreferences
            userTokenPref.edit().clear().apply()

            startActivity<LoginActivity>()
            activity?.finish()
        }

        tv_nama_user.text = userTokenPref.getString(ConstantVariable.fullName, "")
        tv_email.text = userTokenPref.getString(ConstantVariable.email, "")
        // editable edittext
        tv_nama_user2.setText(userTokenPref.getString(ConstantVariable.fullName, ""))
        tv_notelp.setText(userTokenPref.getString(ConstantVariable.nomerHp, ""))
        tv_gender.setText(userTokenPref.getString(ConstantVariable.gender, ""))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(com.perumdajepara.jlajah.R.menu.menu_user, menu)
    }
}
