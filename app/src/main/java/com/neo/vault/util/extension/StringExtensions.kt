package com.neo.vault.util.extension

import com.neo.vault.presentation.model.UiText

fun String.toRaw() = UiText.Raw(this)