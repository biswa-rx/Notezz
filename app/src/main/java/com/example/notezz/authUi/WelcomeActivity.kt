package com.example.notezz.authUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.ui.MainActivity
import com.example.notezz.NotezzApplication
import com.example.notezz.R
import com.example.notezz.databinding.ActivityWelcomeBinding
import com.example.notezz.utils.AccessTokenManager
import com.example.notezz.utils.CustomToast
import com.example.notezz.utils.NetworkUtils
import com.example.notezz.viewmodels.AuthViewModel
import com.example.notezz.viewmodels.AuthViewModelFactory
import javax.inject.Inject

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWelcomeBinding;
    private lateinit var authViewModel: AuthViewModel;
    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    private val TAG = "WelcomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        (application as NotezzApplication).applicationComponent.inject(this)


        authViewModel = ViewModelProvider(this, authViewModelFactory).get(
            AuthViewModel::class.java)

        authViewModel.accessCode.observe(this, Observer {
            gotoMainActivity()
            AccessTokenManager.setAccessToken(it.ACCESS_TOKEN)
            Log.i(TAG, it.ACCESS_TOKEN+"\n"+it.REFRESH_TOKEN)
        })
        authViewModel.errorMessage.observe(this, Observer {
            CustomToast.makeToast(this,it.error.message)
        })
        if(!NetworkUtils.isInternetAvailable(applicationContext)) {
            if(AccessTokenManager.getRefreshToken().isNotEmpty()){
                Handler(Looper.getMainLooper()).post(Runnable {
                    CustomToast.makeToast(applicationContext,"You are Offline\nSync failed")
                })
                gotoMainActivity()
            } else {
                CustomToast.makeToast(this,"You are Offline")
            }
        }
        initOnClickEvent()
    }

    private fun initOnClickEvent() {
        binding.buttonSignup.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        })
        binding.buttonLogin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        })
    }
    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}