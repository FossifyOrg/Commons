package org.fossify.commons.interfaces

interface HashListener {
    fun receivedHash(hash: String, type: Int)
}
