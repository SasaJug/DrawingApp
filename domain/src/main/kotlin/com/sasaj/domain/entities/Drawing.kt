package com.sasaj.domain.entities

/**
 * Created by sjugurdzija on 4/21/2017.
 */

data class Drawing(val id: Long? = 0, val fileName: String = "", val imagePath: String = "", val lastModified: Long = 0) : Comparable<Drawing> {

    override fun compareTo(other: Drawing): Int {
        val compareQuantity = other.lastModified
        return (compareQuantity - this.lastModified).toInt()
    }
}
