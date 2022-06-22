package com.neo.vault.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

object VibratorCompat {

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun vibrate(vibrator: Vibrator, millis: Long) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    millis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            );
        } else {
            vibrator.vibrate(millis);
        }
    }

    fun getInstance(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(
                Context.VIBRATOR_MANAGER_SERVICE
            ) as VibratorManager

            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}