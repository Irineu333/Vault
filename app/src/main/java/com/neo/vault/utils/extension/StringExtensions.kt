package com.neo.vault.utils.extension

import com.neo.vault.presentation.model.UiText

fun String.toRaw() = UiText.Raw(this)