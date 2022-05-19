package com.neo.vault.util.extension

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.checkToShow(
    manager: FragmentManager,
    tag: String = this.javaClass.simpleName,
) {
    val fragment = manager.findFragmentByTag(tag)

    if (fragment?.isVisible != true) {
        show(manager, tag)
    }
}
