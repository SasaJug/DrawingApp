package com.sasaj.graphics.drawingapp.authentication.states

data class LoginViewState(
        var loading : Boolean = false,
        var username : String = ""
)