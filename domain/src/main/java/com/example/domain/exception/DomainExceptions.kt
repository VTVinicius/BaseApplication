package com.example.domain.exception

open class DomainException(message: String, title: String? = null) :
    RuntimeException(message, RuntimeException(title))

sealed class ParamException(message: String, title: String? = null) :
    DomainException(message, title)

class MissingParamsException : ParamException("Params must not be null")
class EmptyFieldException : ParamException("Campo obrigatório")


data class Message(
    val code: String,
    val text: String
)

open class DataSourceException(
    message: String,
    cause: Throwable? = null,
    val code: String? = null
) : Exception(message, cause)
