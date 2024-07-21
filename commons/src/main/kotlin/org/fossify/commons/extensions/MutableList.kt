package org.fossify.commons.extensions

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 == index2) return
    this[index1] = this[index2].also {
        this[index2] = this[index1]
    }
}

fun <T> MutableList<T>.move(currentIndex: Int, newIndex: Int) {
    require(currentIndex in indices) { "currentIndex is out of bounds" }
    require(newIndex in indices) { "newIndex is out of bounds" }
    if (currentIndex == newIndex) return

    val itemToMove = this[currentIndex]
    removeAt(currentIndex)
    add(newIndex, itemToMove)
}
