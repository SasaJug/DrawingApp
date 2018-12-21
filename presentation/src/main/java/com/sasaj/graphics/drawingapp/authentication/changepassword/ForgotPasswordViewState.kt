package com.sasaj.graphics.drawingapp.authentication.changepassword

data class ForgotPasswordViewState(
        var passwordChangeStarted : Boolean  = false,
        var loading : Boolean = false,
        var isPasswordChangeRequested : Boolean = false,
        var isPasswordChanged : Boolean = false
)