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
                Log.d("what","back")
                supportFragmentManager.popBackStack()
            }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        Log.d("what","back2")
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

            if (it == 0) {
                fragmentNavigation(HomeFragment())

            }
            if (it == 1) {
                fragmentNavigation(HospitalBookingFragment())
//                bottomNavigationView.selectedItemId = R.id.booking

            }
            if (it == 2) {
                fragmentNavigation(NotificationFragment())

            }
            if (it == 3) {
                fragmentNavigation(PersonalFragment())
            }
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (viewModel.currentTab.value != 0) {
                        viewModel.changeTab(0)
                    }
                    true
                }

                R.id.booking -> {
                    if (viewModel.currentTab.value != 1) {
                        viewModel.changeTab(1)
                    }
                    true
                }

                R.id.notification -> {
                    if (viewModel.currentTab.value != 2) {
                        viewModel.changeTab(2)
                    }
                    true
                }

                R.id.personal -> {
                    if (viewModel.currentTab.value != 3) {
                        viewModel.changeTab(3)
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