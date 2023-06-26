package com.example.notezz.authUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.NotezzApplication
import com.example.notezz.R
import com.example.notezz.databinding.ActivityLoginBinding
import com.example.notezz.viewmodels.AuthViewModel
import com.example.notezz.viewmodels.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding;
    private lateinit var authViewModel: AuthViewModel;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        val authRepository = (application as NotezzApplication).authRepository

        authViewModel = ViewModelProvider(this, AuthViewModelFactory(authRepository)).get(
            AuthViewModel::class.java)

        authViewModel.accessCode.observe(this, Observer {
            Toast.makeText(this,it.ACCESS_TOKEN+"\n"+it.REFRESH_TOKEN, Toast.LENGTH_LONG).show()
        })

        initOnClickEvent()
    }
    fun initOnClickEvent() {
        binding.buttonSignup.setOnClickListener(View.OnClickListener {
            gotoSignupActivity()
        })
        binding.buttonLogin.setOnClickListener(View.OnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            authViewModel.login(email,password);
        })
    }
    private fun gotoSignupActivity() {
        startActivity(Intent(this,SignupActivity::class.java))
        finish()
    }
}