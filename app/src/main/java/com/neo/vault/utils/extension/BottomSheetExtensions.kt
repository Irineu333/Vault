package com.neo.vault.utils.extension

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

val BottomSheetDialogFragment.behavior get() = view?.let { BottomSheetBehavior.from(it.parent as View) }

fun BottomSheetBehavior<*>.expanded() {
    this.state = BottomSheetBehavior.STATE_EXPANDED
}
