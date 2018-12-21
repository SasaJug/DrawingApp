package com.sasaj.graphics.drawingapp.authentication.register

data class RegisterViewState(
        var registrationStarted : Boolean  = false,
        var loading : Boolean = false,
        var isConfirmed: Boolean = false
)