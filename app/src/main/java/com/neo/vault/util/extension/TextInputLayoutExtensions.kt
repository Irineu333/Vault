package com.neo.vault.util.extension

import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun TextInputLayout.addValidationListener(
    validation: suspend (value: String) -> ValidationResult,
    isInvalid: TextInputLayout.(String) -> Unit = {},
    isValid: TextInputLayout.() -> Unit = {}
): TextWatcher {
    val textInputLayout = this
    var job: Job? = null

    return editText?.addTextChangedListener { value ->
        if (value != null) {
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                when (val result = validation("$value")) {
                    is ValidationResult.IsInvalid -> {
                        isInvalid(textInputLayout, result.message)
                    }
                    ValidationResult.IsValid -> {
                        isValid(textInputLayout)
                    }
                }
            }
        }
    } ?: throw IllegalArgumentException("an edittext is required")
}

sealed class ValidationResult {

    object IsValid : ValidationResult()

    data class IsInvalid(
        val message: String
    ) : ValidationResult()
}

fun TextInputLayout.removeTextWatcher(textWatcher: TextWatcher) {
    editText?.removeTextChangedListener(textWatcher)
}