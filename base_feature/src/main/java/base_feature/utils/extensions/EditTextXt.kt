package base_feature.utils.extensions

import android.text.InputFilter
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener

fun EditText.doOnSubmit(submit: (query: String) -> Unit) {
    setOnEditorActionListener { textView, _, _ ->
        submit(textView.text.toString())
        true
    }
}

fun EditText.clearErrorAfterTyping(afterClear: () -> Unit = {}) = addTextChangedListener {
    if (!it?.toString().isNullOrBlank()) {
        error = null
        afterClear()
    }
}

fun EditText.setMaxLength(maxLength: Int) {
    filters =
        filters.toMutableList().apply { add(InputFilter.LengthFilter(maxLength)) }.toTypedArray()
}

fun CharSequence.insertOrUpdateCharPosition(position: Int, char: String): String {

    if (this.isBlank()) return ""

    if (position > this.length || position < 0) return this.toString()

    val cleanedString = this.replace(Regex(char), "")

    val diffPosition = position - (this.length - cleanedString.length)

    val sb: StringBuilder = StringBuilder()

    cleanedString.forEachIndexed { index, c -> if (index <= diffPosition) sb.append(c) }
    sb.append(char)
    cleanedString.forEachIndexed { index, c -> if (index > diffPosition) sb.append(c) }

    return sb.toString()
}

fun EditText.changeFont(fontId: Int ) {
    typeface = ResourcesCompat.getFont(context, fontId)
}