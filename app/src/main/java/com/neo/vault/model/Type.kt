package com.neo.vault.model

import androidx.annotation.DrawableRes

data class Type(
    @DrawableRes
    val icon: Int,
    val title: String,
    val description: String
)