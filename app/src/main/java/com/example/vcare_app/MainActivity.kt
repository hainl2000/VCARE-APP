package com.example.vcare_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.vcare_app.booking.BookingFragment
import com.example.vcare_app.home.HomeFragment
import com.example.vcare_app.notification.NotificationFragment
import com.example.vcare_app.personal.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        fragmentNavigation(HomeFragment(), "home")

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentNavigation(HomeFragment(), "home")
                    true
                }

                R.id.booking -> {
                    fragmentNavigation(BookingFragment(), "booking")
                    true
                }

                R.id.notification -> {
                    fragmentNavigation(NotificationFragment(), "notification")
                    true
                }

                R.id.personal -> {
                    fragmentNavigation(PersonalFragment(), "personal")
                    true
                }

                else -> false
            }
        }
    }

    private fun fragmentNavigation(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(tag)
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
    }
}