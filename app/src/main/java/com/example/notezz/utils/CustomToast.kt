package com.example.notezz.utils

import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.notezz.R

object CustomToast {
    fun makeToast(context: Context, text: String) {
        android.os.Handler(Looper.getMainLooper()).post(Runnable {
            val view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
            view.findViewById<TextView>(R.id.textViewToast).text = text;

            val toast = Toast(context)
            toast.setView(view)
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.END, 0, 0)
            toast.setGravity(Gravity.BOTTOM, 32, 32)
            toast.show()
        })
    }
}