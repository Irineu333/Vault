package com.neo.vault.util.extension

fun <E> List<E>.summation(sum : (E) -> Double): Double {

    var result = 0.0

    for (element in this) {
        result += sum(element)
    }

    return result
}