package com.sasaj.domain.entities

data class Brush(var id: Long = 0,
                 var size: Int = 5,
                 var blur: Float = 0f,
                 var color: Int = 0xff000000.toInt())