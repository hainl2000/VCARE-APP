package com.example.vcare_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.vcare_app.base.BaseActivity
import com.example.vcare_app.present.booking.BookingFragment
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.present.home.HomeFragment
import com.example.vcare_app.present.notification.NotificationFragment
import com.example.vcare_app.present.personal.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container_view, HomeFragment())
            commit()
        }

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