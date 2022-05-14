package base_feature.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

object SpannableStringFactory {

    fun addMessage(message: String): SpannableString {
        return SpannableString(message)
    }

}

fun TextView.setSpannable(f: (SpannableString) -> SpannableString) {
    text = f(SpannableString(text.toString()))
}

fun SpannableString.addEndDrawable(
    context: Context,
    drawableRes: Int
): SpannableString {

    val newSpannable = SpannableString("$this ")
    val imageSpan = ImageSpan(context, drawableRes)
    newSpannable.setSpan(
        imageSpan,
        newSpannable.length - 1,
        newSpannable.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return newSpannable
}

fun SpannableString.addDrawableWithNoBoundsAt(
    context: Context,
    start: Int = 0,
    end: Int = 0,
    drawableRes: Int
): SpannableString {
    val drawable = ContextCompat.getDrawable(context, drawableRes) ?: throw RuntimeException()

    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val span = ImageSpan(drawable)
    this.setSpan(span, start, end, 0)

    return this
}

fun SpannableString.changeColorAt(
    start: Int,
    end: Int,
    color: Int
): SpannableString {
    try {
        setSpan(
            ForegroundColorSpan(color),
            start, end,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
    return this
}

fun SpannableString.changeFontAt(
    start: Int,
    end: Int,
    styleSpan: StyleSpan
): SpannableString {
    this.setSpan(
        styleSpan,
        start, end,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return this
}

fun SpannableString.clickPartOfTextAt(
    start: Int,
    end: Int,
    isUnderline: Boolean = true,
    onClick: View.OnClickListener
): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            textView.invalidate()
            onClick.onClick(textView)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = isUnderline
        }

    }
    this.setSpan(
        clickableSpan,
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

fun SpannableString.changeFontSizeAt(
    start: Int,
    end: Int,
    fontSize: Float
): SpannableString {
    this.setSpan(
        RelativeSizeSpan(fontSize),
        start, end,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )

    return this
}

interface PresentationMapper<P, D> {

    fun toDomain(presentation: P): D

    fun fromDomain(domain: D): P

    fun toDomain(presentation: List<P>): List<D> {
        return presentation.map { toDomain(it) }
    }

    fun fromDomain(domain: List<D>): List<P> {
        return domain.map { fromDomain(it) }
    }

}