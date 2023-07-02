package com.example.notezz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.notezz.databinding.ActivityArchiveBinding

class ArchiveActivity : AppCompatActivity() {
    lateinit var binding: ActivityArchiveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_archive)
        setSupportActionBar(binding.archiveToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back) // Replace with your back arrow icon
        }
        binding.archiveToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}