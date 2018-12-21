package com.sasaj.graphics.drawingapp.authentication.register

data class VerifyViewState(
        var verificationStarted : Boolean  = false,
        var loading : Boolean = false,
        var isVerified: Boolean = false
)