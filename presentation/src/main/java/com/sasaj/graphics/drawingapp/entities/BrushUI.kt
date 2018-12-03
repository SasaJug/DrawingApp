package com.sasaj.graphics.drawingapp.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BrushUI(
    var size: Int = 5,
    var blur: Float = 0f,
    var color: Int = 0xff000000.toInt()) : Parcelable