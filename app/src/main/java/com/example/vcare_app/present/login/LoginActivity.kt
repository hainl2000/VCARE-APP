package com.example.vcare_app.present.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.MainActivity
import com.example.vcare_app.R
import com.example.vcare_app.base.BaseActivity
import com.example.vcare_app.base.BaseViewModelFactory
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.present.login.signin.SignInFragment
import com.example.vcare_app.present.login.signup.SignUpFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.google.android.material.tabs.TabLayout

class LoginActivity : BaseActivity(R.layout.activity_login) {

    private lateinit var viewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSharePref()
        viewModel =
            ViewModelProvider(this)[LoginActivityViewModel::class.java]
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.login_fragment_container, SignInFragment())
            commit()
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.login_fragment_container, SignInFragment())
                            commit()
                        }
                    }

                    1 -> {
                        supportFragmentManager.beginTransaction().apply {
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
}