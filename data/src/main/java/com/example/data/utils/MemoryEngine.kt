package com.example.data.utils

class MemoryEngine: HashMap<String, Any>() {

    fun putString(key: String, value: String) {
        putValue(key, value)
    }

    fun getString(key: String): String? {
        return getValueById(key)?.castOrNull()
    }

    fun putBoolean(key: String, value: Boolean) {
        putValue(key, value)
    }

    fun getBoolean(key: String): Boolean? {
        return getValueById(key)?.castOrNull()
    }

    fun putInt(key: String, value: Int) {
        putValue(key, value)
    }

    fun getInt(key: String): Int? {
        return getValueById(key)?.castOrNull()
    }

    fun putValue(key: String, value: Any) {
        this[key] = value
    }

    fun getValueById(key: String): Any? {
        if (this.containsKey(key)) {
            return this[key]
        }

        return null
    }

    fun removeById(key: String) {
        this.remove(key)
    }

    inline fun <reified T : Any> getValue(key: String): T? = this.getValueById(key)?.safeCast()
}

inline fun <reified T : Any> Any.castOrNull() = this as T

inline fun <reified T : Any> Any.safeCast(): T? {
    require(this is T)
    return this
}
