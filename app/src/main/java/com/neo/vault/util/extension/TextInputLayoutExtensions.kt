package com.neo.vault.util.extension

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun TextInputLayout.addValidationListener(
    validation: suspend (value: String) -> ValidationResult,
    isInvalid: TextInputLayout.(String) -> Unit = {},
    isValid: TextInputLayout.() -> Unit = {}
) {
    val textInputLayout = this

    editText?.addTextChangedListener { value ->
        if (value != null) {
            CoroutineScope(Dispatchers.Main).launch {
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