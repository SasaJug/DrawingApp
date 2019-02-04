package com.sasaj.domain.entities

import java.util.*

/**
 * Created by sjugurdzija on 4/21/2017.
 */

data class Drawing(val id: Long? = -1,
                   val fileName: String = "",
                   val imagePath: String = "",
                   val lastModified: Long = 0){

    override fun equals(other: Any?): Boolean {
        return if(other == null || other !is Drawing){
            false
        } else {
            fileName == other.fileName
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(fileName)
    }
}