package com.sasaj.graphics.drawingapp.entities

data class DrawingUI (val id: Long? = 0, val fileName: String = "", val imagePath: String = "", val lastModified: Long = 0) : Comparable<DrawingUI>{

    override fun compareTo(other: DrawingUI): Int {
        return when {
            other.lastModified > this.lastModified -> 1
            other.lastModified < this.lastModified -> -1
            else -> 0
        }
    }
}