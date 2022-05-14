package com.example.domain.utils

import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

fun String.unmask() = this.replace("[^\\d]".toRegex(), "")

fun String.isEmail() = RegexEnum.EMAIL.match(this)
fun String.isNotEmail() = !isEmail()
fun String.isIncompleteEmail() = this.endsWith("@")

fun String.isPhoneNumber() = RegexEnum.PHONE_NUMBER.match(this)
fun String.isNotPhoneNumber() = !isPhoneNumber()

fun String.isCep() = length == 8
fun String.isNotCep() = !isCep()

fun String.unAccent(): String {
    val nfdNormalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("") ?: ""
}

fun String.puttingCpfMask(): String {
    return replace(RegexEnum.CPF.value) { matchResult ->
        matchResult.groupValues.mapIndexedNotNull { index, value ->
            when {
                value == this -> null
                matchResult.groupValues.lastIndex == index -> "-${value}"
                matchResult.groupValues.lastIndex - 1 == index -> value
                else -> "${value}."
            }
        }.joinToString(separator = "", truncated = "")
    }
}

fun String.puttingCnpjMask(): String {
    return replace(RegexEnum.CNPJ.value) { matchResult ->
        matchResult.groupValues.mapIndexedNotNull { index, value ->
            when {
                value == this -> null
                matchResult.groupValues.lastIndex == index -> "-${value}"
                matchResult.groupValues.lastIndex - 1 == index -> value
                else -> "${value}."
            }
        }.joinToString(separator = "", truncated = "").replaceRange(10, 11, "/")
    }
}

fun String.isNotValidName() = !isValidName()
fun String.isValidName() = RegexEnum.NAME.match(this)
fun String.isNotCompoundName() = this.split(" ").filter { fullNameParts ->
    fullNameParts != "" && fullNameParts.length >= 1
}.joinToString().split(" ").size < 2

fun String.isDate() = try {
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).also {
        it.isLenient = false
        it.parse(this)
    }
    RegexEnum.DATE.value.matches(this)
} catch (e: Exception) {
    false
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun checkAgeIsValid(birthday: Date): Boolean {
    val range = 18..120
    val diff: Long = getCurrentDateTime().time - birthday.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val year = days / 365
    return year.toInt() in range
}

fun String.isInsideValidPeriod(birthday: Date): Boolean = checkAgeIsValid(birthday)

fun String.isNotDate() = !isDate()

fun String.isRepeating() = toCharArray().toSet().size == 1

fun String.isNotCpf() = !isCpf()

fun String.isNotCnpj() = !isCnpj()

fun String.isNotValidDigitalPasswordDigits() = !isValidDigitalPasswordDigits()

fun String.removeNotNumbers() = this.replace("[^\\d]".toRegex(), "")

fun String.convertToDouble() = replace("""[^(\d,)]""".toRegex(), "")
    .replace(",", ".").toDouble()

fun String.getStateCode() = this.substring(0, 2)

fun String.getNumber() = this.substring(2, length)

fun String.isCnpj(): Boolean {
    if (this.length != 14) return false

    var allDigitsEqual = true
    this.forEach {
        if (this.first() != it) {
            allDigitsEqual = false
        }
    }
    if (allDigitsEqual) return false

    return try {
        val weight = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val cnpjSub = this.substring(0, 12)
        val digit1 = cnpjSub.validateDigit(weight)
        val digit2 = (cnpjSub + digit1).validateDigit(weight)

        this == cnpjSub.plus(digit1).plus(digit2)
    } catch (error: InputMismatchException) {
        false
    }
}

fun String.isValidDigitalPasswordDigits() =
    this.length >= 6

fun String.isCpf(): Boolean {
    if (this.length != 11) return false

    var allDigitsEqual = true
    this.forEach {
        if (this.first() != it) {
            allDigitsEqual = false
        }
    }
    if (allDigitsEqual) return false

    return try {
        val weight = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)
        val cpfSub = this.substring(0, 9)
        val digit1 = cpfSub.validateDigit(weight)
        val digit2 = (cpfSub + digit1).validateDigit(weight)

        this == cpfSub.plus(digit1).plus(digit2)
    } catch (error: InputMismatchException) {
        false
    }
}

private fun String.validateDigit(weight: IntArray): Int {
    var valid = 0
    var index = this.length - 1
    var digit: Int

    while (index >= 0) {
        digit = this.substring(index, index + 1).toInt()
        valid += digit * weight[weight.size - this.length + index]
        index--
    }

    valid = 11 - valid % 11

    return if (valid > 9) {
        0
    } else {
        valid
    }
}

fun String.puttingAccountNumberMask(): String {
    if (isBlank() || getOrNull(lastIndex - 1) == '-')
        return this
    val stringList = removeNotNumbers().toMutableList()
    stringList.add(lastIndex, '-')
    return stringList.joinToString(separator = "", truncated = "")
}

fun String.toBarcode(): String {
    if (isEmpty()) return "--"

    val out = StringBuffer()
    var j = 0
    val mask = "#####.##### #####.###### #####.###### # ##############"
    for (i in mask.indices) {
        when {
            mask[i] == '#' -> {
                out.append(this[j])
                j++
            }
            mask[i] == '.' -> out.append('.')
            else -> out.append(' ')
        }
    }
    return out.toString()
}

fun java.lang.StringBuilder.insertSafe(offset: Int, str: String) = try {
    insert(offset, str)
} catch (e: IndexOutOfBoundsException) {
    this
}

fun String.formatAsPhoneNumber() = StringBuilder(this)
    .insertSafe(0, "(")
    .insertSafe(3, ")")
    .insertSafe(4, " ")
    .insertSafe(6, " ")
    .insertSafe(11, "-").toString()

fun String.formatAsPhoneNumberWithObscureChar() = StringBuilder(formatAsPhoneNumber())
    .replace(7, 12, "**** ").toString()

fun String.cleanPhoneNumberMask() = this.replace("(", "")
    .replace(")", "")
    .replace("-", "")
    .replace(" ", "")

fun String.convertDoubleStringToIntString(): String {
    if (isBlank() || length <= 1 || !contains('.')) return this
    return try {
        val doubleValue = this.toDouble()
        val intValue = doubleValue.roundToInt()
        intValue.toString()
    } catch (e: Exception) {
        this
    }
}

fun String.isEqualNumbers() = this.matches("""([0-9])\1+""".toRegex())

fun String.checkHasEqualNumber(): Boolean {
    for (i in 0 until this.length - 1) {
        if (this[i] == this[i + 1]) {
            return true
        }
    }
    return false
}

fun String.checkSequentialNumber(): Boolean {
    var crescente = 0
    var decrescente = 0
    for (i in 0 until this.length - 1) {
        if (this[i] + 1 == this[i + 1]) {
            crescente++
            if (crescente == 3) {
                return true
            }
        } else if (this[i] - 1 == this[i + 1]) {
            decrescente++
            if (decrescente == 3) {
                return true
            }
        }
    }
    return false
}


fun String.isSequentialNumber() = this.length > 1 && ("0123456789".contains(this)
        || "9876543210".contains(this))

fun String.capitalized(): String {
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else
            it.toString()
    }
}

fun String.capSentences(): String {
    return this.split(" ").joinToString(" ") {
        it.capitalized()
    }
}

fun String.cleanCepMask() = this.replace("-", "")
