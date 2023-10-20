package com.example.vcare_app.login

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
import com.example.vcare_app.base.BaseViewModelFactory
import com.example.vcare_app.login.signin.SignInFragment
import com.example.vcare_app.login.signup.SignUpFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel =
            ViewModelProvider(this)[LoginActivityViewModel::class.java]

        val btnSignIn = findViewById<Button>(R.id.sign_in_btn)
        val btnSignUp = findViewById<Button>(R.id.sign_up_btn)
        btnSignIn.setBackgroundResource(R.drawable.app_button_background_clicked)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.login_fragment_container, SignInFragment())
            commit()
        }
        btnSignIn.setOnClickListener {
            it.setBackgroundResource(R.drawable.app_button_background_clicked)
            btnSignUp.background =
                AppCompatResources.getDrawable(this, R.drawable.app_button_background)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.login_fragment_container, SignInFragment())
                commit()
            }
        }
        btnSignUp.setOnClickListener {
            it.setBackgroundResource(R.drawable.app_button_background_clicked)
            btnSignIn.background =
                AppCompatResources.getDrawable(this, R.drawable.app_button_background)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.login_fragment_container, SignUpFragment())
                commit()
            }
        }
    }
}