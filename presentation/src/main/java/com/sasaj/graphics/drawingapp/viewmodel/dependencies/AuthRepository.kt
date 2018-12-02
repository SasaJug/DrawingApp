package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import io.reactivex.subjects.Subject

interface AuthRepository {

    fun getCheckLoggedInSubject(): Subject<String>
    fun getLoginSubject(): Subject<String>
    fun getRegisterSubject(): Subject<Boolean>
    fun getVerifySubject(): Subject<Boolean>

    fun checkIfLoggedIn()
    fun logIn(username : String?, password : String?)
    fun signUp(username : String?, password : String?, attr : HashMap<String, String>)
    fun verify(username : String?, code : String?)
}