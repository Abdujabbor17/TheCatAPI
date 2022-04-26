package com.masterandroid.thecatapi_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.masterandroid.thecatapi_app.R
import com.masterandroid.thecatapi_app.fragment.HomeFragment
import com.masterandroid.thecatapi_app.fragment.MyImagesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        bottomNavigation.background = null

        loadFragment(HomeFragment())

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.search -> {
                    loadFragment(HomeFragment())
                    true
                }
                else -> {
                    loadFragment(MyImagesFragment())
                    true
                }
            }
        }

        fab.setOnClickListener {
            openUploadActivity()
        }
    }

    private fun openUploadActivity() {
        val intent = Intent(this,UploadActivity::class.java)
        startActivity(intent)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}