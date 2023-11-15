package com.example.vcare_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.base.BaseActivity
import com.example.vcare_app.present.booking.hospitalbooking.HospitalBookingFragment
import com.example.vcare_app.present.home.HomeFragment
import com.example.vcare_app.present.notification.NotificationFragment
import com.example.vcare_app.present.personal.PersonalFragment
import com.example.vcare_app.utilities.TabItem
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                Log.d("what", "back")
                supportFragmentManager.popBackStack()
            }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        Log.d("what", "back2")
                        supportFragmentManager.popBackStack()
                    }
                })
        }

        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            add(R.id.fragment_container_view, HomeFragment())

            commit()
        }
        viewModel.currentTab.observe(this) {
            bottomNavigationView.menu.getItem(it).isChecked = true

            if (it == TabItem.Home.ordinal) {
                fragmentNavigation(HomeFragment())

            }
            if (it == TabItem.Booking.ordinal) {
                fragmentNavigation(HospitalBookingFragment())

            }
            if (it == TabItem.Notification.ordinal) {
                fragmentNavigation(NotificationFragment())

            }
            if (it == TabItem.Personal.ordinal) {
                fragmentNavigation(PersonalFragment())
            }
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (viewModel.currentTab.value != TabItem.Home.ordinal) {
                        viewModel.changeTab(TabItem.Home.ordinal)
                    }
                    true
                }

                R.id.booking -> {
                    if (viewModel.currentTab.value != TabItem.Booking.ordinal) {
                        viewModel.changeTab(TabItem.Booking.ordinal)
                    }
                    true
                }

                R.id.notification -> {
                    if (viewModel.currentTab.value != TabItem.Notification.ordinal) {
                        viewModel.changeTab(TabItem.Notification.ordinal)
                    }
                    true
                }

                R.id.personal -> {
                    if (viewModel.currentTab.value != TabItem.Personal.ordinal) {
                        viewModel.changeTab(TabItem.Personal.ordinal)
                    }
                    true
                }

                else -> false
            }
        }
    }

    val allow = false

    private fun fragmentNavigation(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
    }
}