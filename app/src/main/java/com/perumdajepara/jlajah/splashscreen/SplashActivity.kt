package com.perumdajepara.jlajah.splashscreen

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.login.LoginActivity
import com.perumdajepara.jlajah.main.MainActivity
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.setLocale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // panggil user sharedpreferences dan cek status user
        val userPref = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        // get item userpref
        val status = userPref.getBoolean(ConstantVariable.status, false)
        val lang = userPref.getString(ConstantVariable.myLang, ConstantVariable.indonesia)

        // set bahasa
        setLocale(this, lang as String)

        GlobalScope.launch {
            delay(2000)
            when (status) {
                true -> {
                    startActivity<MainActivity>()
                    finish()
                }
                false -> {
                    startActivity<LoginActivity>()
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        // disable backbutton
    }
}
