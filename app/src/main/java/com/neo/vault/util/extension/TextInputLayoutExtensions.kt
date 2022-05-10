package com.neo.vault.util.extension

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.addValidationListener(
    validation: (value: String) -> ValidationResult,
    isInvalid: TextInputLayout.(String) -> Unit = {},
    isValid: TextInputLayout.() -> Unit = {}
) {

    editText?.addTextChangedListener { value ->
        if (value != null) {

            when (val result = validation("$value")) {
                is ValidationResult.isInvalid -> {
                    isInvalid(this, result.message)
                }
                ValidationResult.isValid -> {
                    isValid(this)
                }
            }
        }
    } ?: throw IllegalArgumentException("an edittext is required")
}

sealed class ValidationResult {

    object isValid : ValidationResult()
    data class isInvalid(
        val message: String
    ) : ValidationResult()
}