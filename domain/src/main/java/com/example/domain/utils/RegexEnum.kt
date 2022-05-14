package com.example.domain.utils

enum class RegexEnum(val value: Regex) {
    PASSWORD("""(\d{6})""".toRegex()),
    CNPJ("""(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})""".toRegex()),
    EMAIL("""^[a-zA-Z0-9+._%\-+]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+""".toRegex()),
    CPF("""(\d{3})(\d{3})(\d{3})(\d{2})""".toRegex()),
    DATE("""^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$""".toRegex()),
    PHONE_NUMBER("""^\([1-9]{2}\) 9 [1-9][0-9]{3}-[0-9]{4}$""".toRegex()),
    NAME("""(^[[A-Z][a-z] (áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ)]{3,}$)""".toRegex());

    fun match(string: String) = this.value.containsMatchIn(string)
}