package com.example.vcare_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.base.BaseActivity
import com.example.vcare_app.present.booking.hospitalbooking.HospitalBookingFragment
import com.example.vcare_app.present.home.HomeFragment
import com.example.vcare_app.present.notification.NotificationFragment
import com.example.vcare_app.present.personal.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            add(R.id.fragment_container_view, HomeFragment())

            commit()
        }
        viewModel.currentTab.observe(this) {
            bottomNavigationView.menu.getItem(it).isChecked = true

            if (it == 0) {
                fragmentNavigation(HomeFragment(), "home")

            }
            if (it == 1) {
                fragmentNavigation(HospitalBookingFragment(), "booking")
//                bottomNavigationView.selectedItemId = R.id.booking

            }
            if (it == 2) {
                fragmentNavigation(NotificationFragment(), "notification")

            }
            if (it == 3) {
                fragmentNavigation(PersonalFragment(), "personal")
            }
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    viewModel.changeTab(0)
                    true
                }

                R.id.booking -> {
                    viewModel.changeTab(1)
                    true
                }

                R.id.notification -> {
                    viewModel.changeTab(2)
                    true
                }

                R.id.personal -> {
                    viewModel.changeTab(3)
                    true
                }

                else -> false
            }
        }
    }

    private fun fragmentNavigation(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
    }
}