package base_feature.utils.extensions

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import java.io.Serializable

inline val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
inline val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline val Float.dp: Float get() = (this / Resources.getSystem().displayMetrics.density)
inline val Float.px: Float get() = (this * Resources.getSystem().displayMetrics.density)

fun View.setGone() {
    visibility = View.GONE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun ProgressBar.animatedProgressTo(fromValue: Number, toValue: Number) {
    progress = fromValue.toInt()
    animatedProgressTo(toValue)
}

fun ProgressBar.animatedProgressTo(toValue: Number) {
    ObjectAnimator.ofInt(this, "progress", toValue.toInt())
        .setDuration(300)
        .start()
}


fun withViews(vararg views: View, action: (View) -> Any) {
    views.forEach {
        action(it)
    }
}

fun View.animateAlpha(alpha: Float, duration: Long = 200L) {
    animate().apply {
        alpha(alpha)
        this.duration = duration
        interpolator = AccelerateInterpolator()
        start()
    }
}

fun RadioGroup.addViewAndRun(view: View, index: Int, action: () -> Unit) {
    addView(view, index)
    action()
}

fun ConstraintLayout.setWrapContent() {
    this.layoutParams = ConstraintLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

interface FragmentResultListener<T> : Serializable {

    fun onFragmentResult(data: T?)

}

fun MaterialButton.enable() {
    isEnabled = true
}

fun MaterialButton.disable() {
    isEnabled = false
}