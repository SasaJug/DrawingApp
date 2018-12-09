package com.sasaj.graphics.drawingapp.authentication.states

data class AuthenticationNavigationViewState(
        var state: Int = -1,
        var data: String = ""
) {
    companion object {
        val LOADING: Int  = 0
        val LOGIN_SUCCESFUL: Int = 1
        val GO_TO_LOGIN: Int = 2
        val GO_TO_REGISTER: Int = 3
        val GO_TO_FORGOT_PASSWORD: Int = 4
        val GO_TO_VERIFY: Int = 5
    }

}