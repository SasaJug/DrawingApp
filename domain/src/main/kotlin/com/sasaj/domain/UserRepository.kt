package com.sasaj.domain

import io.reactivex.Observable

interface UserRepository {

    fun checkIfLoggedIn() : Observable<String>
    fun logIn(username : String?, password : String?) : Observable<String>
    fun signUp(username : String?, password : String?, attr : HashMap<String, String>) : Observable<Boolean>
    fun verify(username : String?, code : String?) : Observable<Boolean>

}