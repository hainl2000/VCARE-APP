package com.example.vcare_app.mainactivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.R
import com.example.vcare_app.base.BaseActivity
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.present.appointmentdetail.AppointmentDetailFragment
import com.example.vcare_app.present.booking.hospitalbooking.HospitalBookingFragment
import com.example.vcare_app.present.home.HomeFragment
import com.example.vcare_app.present.personal.PersonalFragment
import com.example.vcare_app.utilities.AppDeepLink
import com.example.vcare_app.utilities.CheckAppAlive
import com.example.vcare_app.utilities.TabItem
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /// for case from other side
        SharePrefManager.init(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        Log.d("MainActivity", "onCreate: create lai ko")
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val argument =
            intent.getParcelableExtra<AppointmentDetailArgument>(AppDeepLink.appointmentDetailArgumentName)
        Log.d("login", "onCreate: ${argument?.appointmentId} a")
        if (argument != null) {
            viewModel.changeTab(TabItem.Personal.ordinal)
            Log.d("login", "onCreate: ${argument.appointmentId} a")
        }

        viewModel.currentTab.observe(this) {
            bottomNavigationView.menu.getItem(it).isChecked = true

            if (it == TabItem.Home.ordinal) {
                fragmentNavigation(HomeFragment())
            }
            if (it == TabItem.Booking.ordinal) {
                fragmentNavigation(HospitalBookingFragment())

            }
            if (it == TabItem.Personal.ordinal) {
                if (argument != null && AppDeepLink.isFromNotification) {
                    AppDeepLink.isFromNotification = false
                    fragmentNavigation(PersonalFragment())
                    supportFragmentManager.beginTransaction().apply {
                        val fragment = AppointmentDetailFragment()
                        fragment.arguments = Bundle().apply {
                            this.putParcelable("appointment_id", argument)
                        }
                        setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        add(R.id.fragment_container_view, fragment)
                        commit()
                    }

                } else {
                    fragmentNavigation(PersonalFragment())
                }
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

    private fun fragmentNavigation(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CheckAppAlive.isAppAlive = false
    }
}