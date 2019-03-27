package com.perumdajepara.jlajah.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.account.AccountFragment
import com.perumdajepara.jlajah.bookmark.BookmarkFragment
import com.perumdajepara.jlajah.home.HomeFragment
import com.perumdajepara.jlajah.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navbottom_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, HomeFragment()).commit()
                R.id.map -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, MapFragment()).commit()
                R.id.bookmark -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, BookmarkFragment()).commit()
                R.id.account -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, AccountFragment()).commit()
            }

            true
        }

        navbottom_main.selectedItemId = R.id.home
    }
}
