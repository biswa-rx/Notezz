package com.example.notezz.utils

import android.graphics.Color

fun convertHexStringToColor(hexString: String): Int {
    // Ensure the hex string is in the correct format
    val formattedHexString = if (hexString.startsWith("#")) hexString else "#$hexString"

    // Parse the color
    return try {
        Color.parseColor(formattedHexString)
    } catch (e: IllegalArgumentException) {
        // Handle invalid color string
        Color.TRANSPARENT
    }

}

fun convertColorToHexString(color: Int): String {
    val alpha = Color.alpha(color)
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)

    return if (alpha == 255) {
        // Opaque color, no need to include alpha in the hex representation
        String.format("#%02X%02X%02X", red, green, blue)
    } else {
        // Include alpha in the hex representation
        String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
    }
}