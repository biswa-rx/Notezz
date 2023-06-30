package com.example.notezz.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat.getSystemService

class CreateVibrationEffect(context: Context) {
    init {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate with the pattern [0, 100, 100, 200] - 0 delay, vibrate for 100ms, pause for 100ms, vibrate for 200ms
        val pattern = longArrayOf(0, 300, 200, 200)
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
    }
}