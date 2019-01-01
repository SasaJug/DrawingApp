package com.sasaj.graphics.drawingapp.common


class UIException(message: String = "Something went wrong", cause: Throwable = Exception()) : RuntimeException(message, cause) {

    companion object {
        const val EMPTY_USERNAME : Int = 1
        const val EMPTY_PASSWORD : Int = 2
    }

   var errorCode: Int = -1

    constructor(message: String, cause: Throwable, errorCode: Int) : this(message, cause){
        this.errorCode = errorCode
    }

    constructor(errorCode: Int) : this()
}