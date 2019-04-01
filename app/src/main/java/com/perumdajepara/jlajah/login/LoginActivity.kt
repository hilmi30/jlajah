package com.perumdajepara.jlajah.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_daftar_btn.onClick {
            startActivity<SignupActivity>()
        }
    }
}
