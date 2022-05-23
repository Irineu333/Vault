package com.neo.vault.utils.extension

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.os.ConfigurationCompat
import com.neo.vault.presentation.model.UiText
import java.util.*

val Context.locale: Locale get() = ConfigurationCompat.getLocales(resources.configuration)[0]

fun Context.showAlertDialog(
    title: UiText,
    message: UiText,
    config: AlertDialog.Builder.() -> Unit
): AlertDialog {
    return AlertDialog.Builder(this).also {

        it.setTitle(title.resolve(this))
        it.setMessage(message.resolve(this))

        config(it)
    }.create().apply {
        show()
    }
}