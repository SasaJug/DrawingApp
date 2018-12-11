package com.sasaj.graphics.drawingapp.authentication.states

data class PasswordChangeViewState(
        var passwordChangeStarted : Boolean  = false,
        var loading : Boolean = false,
        var isPasswordChangeRequested : Boolean = false,
        var isPasswordChanged : Boolean = false
)