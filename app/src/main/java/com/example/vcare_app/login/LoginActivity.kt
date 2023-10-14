package com.example.vcare_app.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import com.example.vcare_app.R
import com.example.vcare_app.login.signin.SignInFragment
import com.example.vcare_app.login.signup.SignUpFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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