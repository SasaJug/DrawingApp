package com.sasaj.domain.entities

/**
 * Created by sjugurdzija on 4/21/2017.
 */

data class Drawing(val id: Long? = -1,
                   val fileName: String = "",
                   val imagePath: String = "",
                   val lastModified: Long = 0){
}