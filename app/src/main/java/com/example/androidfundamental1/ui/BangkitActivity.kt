package com.example.androidfundamental1.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androidfundamental1.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BangkitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bangkit)

        // Inisialisasi Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Mengatur tombol "Up" agar terlihat
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Menghubungkan BottomNavigationView dengan NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Menghubungkan BottomNavigationView dengan NavController
        bottomNavigationView.setupWithNavController(navController)

        // Setup listener untuk BottomNavigationView jika perlu
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.upcomingEventsFragment -> {
                    navController.navigate(R.id.upcomingEventsFragment)
                    true
                }
                R.id.searchEventsFragment -> {
                    // Arahkan ke HomeEventsFragment ketika tombol search ditekan
                    navController.navigate(R.id.homeEventsFragment)
                    true
                }
                R.id.pastEventsFragment -> {
                    navController.navigate(R.id.pastEventsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Mengatur aksi saat tombol "Up" ditekan
        onBackPressed()
        return true
    }
}
