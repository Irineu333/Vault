package com.neo.vault.utils.extension

import android.view.ViewGroup
import androidx.core.view.children

var ViewGroup.childrenIsEnable: Boolean
    set(value) {
        children.forEach { it.isEnabled = value }
    }
    get() {
        return children.all { it.isEnabled }
    }