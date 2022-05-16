package com.neo.vault.util.extension

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

val BottomSheetDialogFragment.behavior get() = (dialog as? BottomSheetDialog)?.behavior

fun BottomSheetBehavior<*>.expanded() {
    this.state = BottomSheetBehavior.STATE_EXPANDED
}
