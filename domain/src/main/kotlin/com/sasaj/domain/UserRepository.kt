package com.sasaj.domain

import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface UserRepository {

    fun checkIfLoggedIn() : Observable<String>
    fun logIn(username : String?, password : String?) : Observable<String>
    fun signUp(username : String?, password : String?, attr : HashMap<String, String>) : Observable<Boolean>
    fun signOut() : Observable<Boolean>
    fun verify(username : String?, code : String?) : Observable<Boolean>
    fun changePassword(username : String?) : Observable<Boolean>
    fun newPassword(newPassword : String?, code : String?) : Observable<Boolean>

}