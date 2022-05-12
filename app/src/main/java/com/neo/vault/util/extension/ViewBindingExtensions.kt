package com.neo.vault.util.extension

import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.neo.vault.presentation.model.UiText

fun ViewBinding.showSnackbar(
    message: UiText,
    length: Int = Snackbar.LENGTH_LONG
) = root.showSnackbar(
    message = message,
    length = length
)