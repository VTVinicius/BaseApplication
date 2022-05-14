package base_feature.utils.extensions

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import base_feature.utils.changeFontSizeAt
import base_feature.utils.clickPartOfTextAt
import base_feature.utils.setSpannable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.text.NumberFormat
import java.util.*

fun TextView.addOnPartClickListener(
    substring: String,
    isUnderline: Boolean = true,
    onClick: () -> Unit
) {

    val start = text.indexOf(substring)

    setSpannable {
        it.clickPartOfTextAt(start, start + substring.length, isUnderline) {
            onClick()
        }
        it
    }

    movementMethod = LinkMovementMethod.getInstance()

    setOnClickListener {
        invalidate()
    }
}

fun TextView.addOnPartClickListener(vararg onClick: TextClick) {

    setSpannable {
        onClick.forEach { e ->
            val start = text.indexOf(e.substring)
            it.clickPartOfTextAt(start, start + e.substring.length, e.isUnderline) {
                e.onClick()
            }
        }
        it
    }

    highlightColor = Color.TRANSPARENT
    movementMethod = LinkMovementMethod.getInstance()

    setOnClickListener {
        invalidate()
    }
}

data class TextClick(
    val substring: String,
    val isUnderline: Boolean = false,
    val onClick: () -> Unit
)

fun TextView.addDrawableStart(drawable: Drawable?){
    setCompoundDrawables(drawable, null, null, null)
}

fun TextView.addDrawableStart(resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(context, resId), null, null, null
    )
}

fun TextView.addDrawableEnd(resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        null, null,  ContextCompat.getDrawable(context, resId), null
    )
}

fun TextView.setReal(real: Double?, format: Boolean = false) {
    val locale = Locale("pt", "BR")
    this.text = NumberFormat.getCurrencyInstance(locale).format(real)
    if (format) currencyFormat()
}

fun TextView.currencyFormat() {
    setSpannable { spannableString ->
        val lastIndex = text.length
        spannableString.changeFontSizeAt(0, 2, 0.5f)
        spannableString.changeFontSizeAt(lastIndex - 3, lastIndex, 0.5f)
    }
}

fun TextView.loadDrawableStart(
    url: String?,
    @DrawableRes resPlaceholder: Int? = null
) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .apply {
            resPlaceholder?.let { placeholder(it) }
        }
        .fitCenter()
        .into(object : Target<Drawable> {
            override fun onStart() = Unit

            override fun onStop() = Unit

            override fun onDestroy() = Unit

            override fun onLoadStarted(placeholder: Drawable?) {
                addDrawableStart(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                addDrawableStart(errorDrawable)
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                addDrawableStart(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) = Unit

            override fun getSize(cb: SizeReadyCallback) = Unit

            override fun removeCallback(cb: SizeReadyCallback) = Unit

            override fun setRequest(request: Request?) = Unit

            override fun getRequest(): Request? = null
        })
}

fun TextView.setStyle(@StyleRes resId: Int) = TextViewCompat.setTextAppearance(this, resId)
