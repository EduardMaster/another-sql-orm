package br.com.eduard.auto_sql.util

fun Array<Class<*>?>.equalsArray( secondArray: Array<Class<*>?>): Boolean {
    if (size == secondArray.size) {
        for (i in secondArray.indices) {
            if (this[i] != secondArray[i]) {
                return false
            }
        }
        return true
    }
    return false
}