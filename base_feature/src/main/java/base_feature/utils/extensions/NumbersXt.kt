package base_feature.utils.extensions

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

fun Number.asCurrency(): String = NumberFormat
    .getCurrencyInstance(Locale("pt", "BR"))
    .format(this.toDouble())

fun String.asCurrencyValue(): Double = try {
    NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .parse(this)?.toDouble() ?: 0.0
} catch (e: ParseException) {
    0.0
}

fun Double.formatValues(): String {
    val format = DecimalFormat("0.######")
    return format.format(this)
}