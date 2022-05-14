package base_feature.utils.extensions

import android.content.res.Resources
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


fun BottomSheetDialogFragment.adjustResize() {
    dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

fun BottomSheetDialogFragment.setStateExpanded() {
    dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.run {
        BottomSheetBehavior.from(this).state = BottomSheetBehavior.STATE_EXPANDED
    }
}

fun BottomSheetDialogFragment.removeDrag() {
    dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.run {
        BottomSheetBehavior.from(this).isDraggable = false
    }
}


fun BottomSheetDialogFragment.setExpandedOffset(@Px offset: Int) {
    dialog?.setOnShowListener {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?.run {
                BottomSheetBehavior.from(this).run {
                    isFitToContents = false
                    expandedOffset = offset
                }
            }
    }
}

fun BottomSheetDialogFragment.setFitToContents(fitToContents: Boolean) {
    dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {
        BottomSheetBehavior.from(it).isFitToContents = fitToContents
    }
}

/**
 * Resize the bottom sheet to have the screen size and change style to have marginTop
 * */
fun BottomSheetDialogFragment.maximized() {
    dialog?.setOnShowListener {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?.apply {
                layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
            }
    }
}

fun BottomSheetDialogFragment.adjustPan() {
    dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun DialogFragment.showBottomSheet(fragment: Fragment) {
    val tag = this::class.java.simpleName
    fragment.childFragmentManager.executePendingTransactions()

    if (fragment.childFragmentManager.findFragmentByTag(tag) == null) {
        try {
            show(fragment.childFragmentManager, tag)
        } catch (e: Exception) {
            Unit
        }
    }
}

fun DialogFragment.showOnce(fragmentManager: FragmentManager) {
    val tag = this::class.java.simpleName
    if (fragmentManager.isDestroyed) return

    if (fragmentManager.findFragmentByTag(tag) == null) {
        show(fragmentManager, tag)
    }
}

fun BottomSheetDialogFragment.closeKeyboard() {
    activity?.window?.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
    )
}