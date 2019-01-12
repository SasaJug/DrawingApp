package com.sasaj.graphics.drawingapp.common


class UIException(override val message: String = "Something went wrong",
                  override val cause: Throwable = Exception(),
                  val errorCode: Int = -1) : RuntimeException(message, cause) {

    companion object {
        const val EMPTY_USERNAME : Int = 1
        const val EMPTY_PASSWORD : Int = 2
        const val EMPTY_EMAIL : Int = 4
        const val EMPTY_CONFIRM_PASSWORD: Int = 8
        const val PASSWORDS_DO_NOT_MATCH: Int = 16
        const val EMPTY_CODE: Int = 32
    }

}