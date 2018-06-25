package com.sasaj.graphics.drawingapp.domain

/**
 * Created by sjugurdzija on 4/21/2017.
 */

data class Drawing(val imagePath: String, val lastModified: Long) : Comparable<Drawing> {

    override fun compareTo(other: Drawing): Int {
        val compareQuantity = other.lastModified
        return (compareQuantity - this.lastModified).toInt()
    }
}
