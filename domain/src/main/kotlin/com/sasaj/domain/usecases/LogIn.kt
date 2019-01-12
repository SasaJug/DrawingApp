package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.HashMap

open class LogIn(
        transformer: Transformer<Boolean>,
        private val userRepository: UserRepository) : UseCase<Boolean>(transformer) {


    companion object {
        private const val PARAM_USERNAME = "param:username"
        private const val PARAM_PASSWORD = "param:password"
    }

    fun logIn(username: String, password: String): Observable<Boolean> {
        val data = HashMap<String, String>()
        data[PARAM_USERNAME] = username
        data[PARAM_PASSWORD] = password
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val username = data?.get(PARAM_USERNAME)
        val password = data?.get(PARAM_PASSWORD)
        return if (username != null && password != null) {
            userRepository.logIn(username as String, password as String)
        } else {
            Observable.error { IllegalArgumentException("Username and password must be provided.") }
        }
    }
}