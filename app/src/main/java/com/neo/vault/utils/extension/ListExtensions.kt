package com.neo.vault.utils.extension

fun <E> List<E>.summation(sum: (E) -> Double): Double {

    var result = 0.0

    for (element in this) {
        result += sum(element)
    }

    return result
}


fun <E> List<E>.lastWithIndex() = lastIndex to last()


fun <E> List<E>.firstWithIndex(predicate: (E) -> Boolean): Pair<Int, E>? {
    val indexOfFirst = indexOfFirst(predicate)

    if (indexOfFirst == -1) return null

    return indexOfFirst to this[indexOfFirst]
}