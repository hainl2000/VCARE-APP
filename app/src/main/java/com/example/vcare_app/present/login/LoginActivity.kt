package com.example.vcare_app.present.login

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.R
import com.example.vcare_app.base.BaseActivity
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.present.login.signin.SignInFragment
import com.example.vcare_app.present.login.signup.SignUpFragment
import com.example.vcare_app.utilities.AppDeepLink
import com.google.android.material.tabs.TabLayout

class LoginActivity : BaseActivity(R.layout.activity_login) {

    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data =
            intent.getParcelableExtra(
                AppDeepLink.appointmentDetailArgumentName

            ) as AppointmentDetailArgument?


        supportFragmentManager.beginTransaction().apply {
            val fragment = SignInFragment()
            fragment.arguments = Bundle().apply {
                this.putParcelable(AppDeepLink.appointmentDetailArgumentName, data)
            }
            replace(R.id.login_fragment_container, fragment)
            commit()
        }


        initSharePref()
        viewModel =
            ViewModelProvider(this)[LoginActivityViewModel::class.java]

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction().apply {
                            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                            replace(R.id.login_fragment_container, SignInFragment())
                            commit()
                        }
                    }

                    1 -> {
                        supportFragmentManager.beginTransaction().apply {
                            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                            replace(R.id.login_fragment_container, SignUpFragment())
                            commit()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewModel.getCurrentIndex.observe(this) {
            tabLayout.getTabAt(it)?.select()
        }
    }

    private fun initSharePref() {
        SharePrefManager.init(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()

    }
}