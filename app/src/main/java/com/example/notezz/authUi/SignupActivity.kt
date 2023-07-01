package com.example.notezz.authUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.MainActivity
import com.example.notezz.NotezzApplication
import com.example.notezz.R
import com.example.notezz.databinding.ActivityMainBinding
import com.example.notezz.databinding.ActivitySignupBinding
import com.example.notezz.utils.AccessTokenManager
import com.example.notezz.utils.CustomToast
import com.example.notezz.viewmodels.AuthViewModel
import com.example.notezz.viewmodels.AuthViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding;
    private lateinit var authViewModel: AuthViewModel;
    private val TAG = "SignupActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        val authRepository = (application as NotezzApplication).authRepository
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(authRepository)).get(AuthViewModel::class.java)

        authViewModel.accessCode.observe(this, Observer {
            gotoMainActivity()
            AccessTokenManager.setAccessToken(it.ACCESS_TOKEN)
            Log.i(TAG, it.ACCESS_TOKEN+"\n"+it.REFRESH_TOKEN)
        })
        authViewModel.errorMessage.observe(this, Observer {
            CustomToast.makeToast(this,it.error.message)
        })
        initOnClickEvent()
    }
    private fun initOnClickEvent(){
        binding.buttonLogin.setOnClickListener(View.OnClickListener {
            gotoLoginActivity();
        })
        binding.buttonSignup.setOnClickListener(View.OnClickListener {
            var name = binding.etUserName.text.toString()
            var email = binding.etUserEmail.text.toString()
            var password = binding.etUserPassword.text.toString()
            authViewModel.resister(name,email,password);
        })
    }
    private fun gotoLoginActivity(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}