package base_feature.utils.extensions

import android.content.Context
import android.graphics.Typeface
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.toSpannable
import com.example.domain.utils.*
import java.lang.Long

fun CharSequence.setSpan(vararg fields: String, span: Any = StyleSpan(Typeface.BOLD)) = toSpannable().apply {
    fields.forEach {
        try {
            val startPosition = indexOf(it, ignoreCase = true)
            val endPosition = startPosition + it.length
            setSpan(
                span,
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        } catch (ignored: Exception) {
            Unit
        }
    }
}

fun CharSequence.setClickSpan(
    text: String,
    click: () -> Unit,
    @ColorRes color: Int? = null,
    @FontRes font: Int? = null,
    context: Context? = null
) = toSpannable().apply {
    try {
        val startPosition = indexOf(text, ignoreCase = true)
        val endPosition = startPosition + text.length
        setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    if (color == null && font == null) {
                        super.updateDrawState(ds)
                    } else {
                        context?.let { context ->
                            color?.let { ds.color = ContextCompat.getColor(context, it) }
                            font?.let {
                                ds.typeface = Typeface.create(
                                    ResourcesCompat
                                        .getFont(context, font), Typeface.BOLD
                                )
                            }
                        }
                    }
                }

                override fun onClick(v: View) {
                    click()
                }
            },
            startPosition,
            endPosition,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    } catch (ignored: Exception) {
        Unit
    }
}


fun String.firstName() = split(" ")[0]

fun String.firstNames(@IntRange(from = 0, to = Long.MAX_VALUE) count: Int) = split(" ").run {
    val size = when (count > size) {
        true -> size
        else -> count
    }
    subList(0, size).joinToString(separator = " ", truncated = "")
}

fun String.isPassword() = RegexEnum.PASSWORD.match(this)

fun String.isNotPassword() = !isPassword()

fun String.initials(length: Int) =
    split(" ")
        .filter { it.isNotBlank() }
        .map { it.first().toUpperCase() }
        .joinToString("")
        .subSequence(
            0,
            if (length > lastIndex) lastIndex else length
        )

fun String.puttingHiddenCpfMask(): String {
    val numbers = removeNotNumbers()
    if (numbers.isCpf()) {
        val prefix = numbers.take(3)
        val suffix = numbers.takeLast(2)
        return "$prefix *** ***-$suffix"
    }
    return ""
}

fun String.puttingHiddenEmailMask(): String {
    if (this.isEmail()) {
        val email = this.split(Regex("(?=@)"))

        val addressPrefix = email[0].take(3)
        val domainPrefix = email[1].take(3)
        val domainSuffix = email[1].substringAfter(".")
        return "$addressPrefix*****$domainPrefix***.$domainSuffix"
    }

    return ""
}

fun String.puttingHiddenPhoneMask(): String {
    if (this.isPhoneNumber()) {
        val prefix = this.take(8)
        val suffix = this.takeLast(2)
        return "$prefix*******$suffix"
    }

    return ""
}

fun String.isEmailHiddenMasked() =
    """([a-zA-Z0-9+._%\-+]+)([*]{5})@([a-zA-Z0-9]+)([*]{3}.[a-zA-Z0-9])""".toRegex()
        .containsMatchIn(this)

fun String.isPhoneHiddenMasked() =
    """^\([1-9]{2}\) [0-9] ([0-9]{1})([*]{7})([0-9]{2})$""".toRegex().containsMatchIn(this)

fun String.isCpfHiddenMasked() =
    """(\d{3}) ([*]{3}) ([*]{3})-(\d{2})""".toRegex().containsMatchIn(this)

fun String.normalizeString(): String = this.unAccent().lowercase()

fun String.convertToNameFormat() : String{
    val nameParts = this.lowercase().split(" ")
    return nameParts.joinToString(separator = " "){it -> it.replaceFirstChar { it.uppercase() }}
}

fun String.convertHttpToHttps() : String{
    return this.replaceBefore("www", "https://")
}