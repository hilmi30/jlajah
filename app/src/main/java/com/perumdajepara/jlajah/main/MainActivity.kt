package com.perumdajepara.jlajah.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.account.AccountFragment
import com.perumdajepara.jlajah.bookmark.BookmarkFragment
import com.perumdajepara.jlajah.home.HomeFragment
import com.perumdajepara.jlajah.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private val fragment1: Fragment = HomeFragment()
    private val fragment2: Fragment = MapFragment()
    private val fragment3: Fragment = BookmarkFragment()
    private val fragment4: Fragment = AccountFragment()
    private var active = fragment1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.frame_main, fragment4).hide(fragment4).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame_main, fragment3).hide(fragment3).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame_main, fragment2).hide(fragment2).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame_main, fragment1).commit()

        navbottom_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.map -> {
                    supportFragmentManager.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bookmark -> {
                    supportFragmentManager.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.account -> {
                    supportFragmentManager.beginTransaction().hide(active).show(fragment4).commit()
                    active = fragment4
                    return@setOnNavigationItemSelectedListener true
                }
            }

            false
        }

        navbottom_main.selectedItemId = R.id.home
    }
}
