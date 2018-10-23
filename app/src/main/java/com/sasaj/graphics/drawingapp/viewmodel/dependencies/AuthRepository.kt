package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import io.reactivex.subjects.Subject

interface AuthRepository {
    fun getAuthenticationSubject(): Subject<String>
    fun checkIfLoggedIn()
}