package com.perumdajepara.jlajah.splashscreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.login.LoginActivity
import com.perumdajepara.jlajah.main.MainActivity
import com.perumdajepara.jlajah.util.ConstantVariable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var userTokenSharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // panggil sharedpreferences
        val userTokenPref = getSharedPreferences(ConstantVariable.userToken, Context.MODE_PRIVATE)

        // cek status user token
        val statusToken = userTokenPref.getBoolean(ConstantVariable.statusToken, false)

        GlobalScope.launch {
            delay(2000)
            when (statusToken) {
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
