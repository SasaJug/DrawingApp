package com.sasaj.graphics.drawingapp.authentication.states

data class AuthenticationNavigationViewState(
        var state: Int = -1,
        var data: String = ""
) {
    companion object {
        const val LOADING: Int = 0
        const val LOGIN_SUCCESSFUL: Int = 1
        const val REGISTRATION_CONFIRMED: Int = 2
        const val REGISTRATION_NOT_CONFIRMED: Int = 3
        const val VERIFICATION_CONFIRMED: Int = 4

        const val GO_TO_REGISTER: Int = 13
        const val GO_TO_FORGOT_PASSWORD: Int = 14
        const val GO_TO_VERIFY: Int = 15
    }

}