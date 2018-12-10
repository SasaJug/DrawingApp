package com.sasaj.graphics.drawingapp.authentication.states

data class RegisterViewState(
        var registrationStarted : Boolean  = false,
        var loading : Boolean = false,
        var isConfirmed: Boolean = false
)