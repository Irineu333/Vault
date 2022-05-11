package com.neo.vault.util.extension

import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.neo.vault.presentation.model.UiText

fun ViewBinding.showSnackbar(
    message: UiText,
    length: Int = Snackbar.LENGTH_LONG
): Snackbar {
    return Snackbar.make(
        root,
        message.resolve(root.context),
        length
    ).apply {
        show()
    }
}