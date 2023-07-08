package com.example.notezz.authUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.ui.MainActivity
import com.example.notezz.NotezzApplication
import com.example.notezz.R
import com.example.notezz.databinding.ActivityLoginBinding
import com.example.notezz.repository.AuthRepository
import com.example.notezz.repository.NoteRepository
import com.example.notezz.utils.AccessTokenManager
import com.example.notezz.utils.CustomToast
import com.example.notezz.viewmodels.AuthViewModel
import com.example.notezz.viewmodels.AuthViewModelFactory
import com.example.notezz.viewmodels.NoteViewModel
import com.example.notezz.viewmodels.NoteViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding;
    private lateinit var authViewModel: AuthViewModel;
    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    @Inject
    lateinit var noteViewModelFactory: NoteViewModelFactory
    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        (application as NotezzApplication).applicationComponent.inject(this)


        authViewModel = ViewModelProvider(this, authViewModelFactory).get(
            AuthViewModel::class.java)

        authViewModel.accessCode.observe(this, Observer {
            if (AccessTokenManager.getRefreshToken().toString().isNotEmpty()) {
                ViewModelProvider(this, noteViewModelFactory).get(
                    NoteViewModel::class.java).syncAllNote()
                gotoMainActivity()
            }
                Log.i(TAG, it.ACCESS_TOKEN+"\n"+it.REFRESH_TOKEN)

        })
        authViewModel.errorMessage.observe(this, Observer {
            CustomToast.makeToast(this,it.error.message)
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
    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}