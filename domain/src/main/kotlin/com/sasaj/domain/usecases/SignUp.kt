package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.HashMap

class SignUp(
        transformer: Transformer<Boolean>,
        private val userRepository: UserRepository) : UseCase<Boolean>(transformer) {


    companion object {
        private const val PARAM_USERNAME = "param:username"
        private const val PARAM_PASSWORD = "param:password"
        private const val PARAM_EMAIL = "param:email"
    }

    fun signUp(username: String, password: String, email: String): Observable<Boolean> {
        val data = HashMap<String, String>()
        data[PARAM_USERNAME] = username
        data[PARAM_PASSWORD] = password
        data[PARAM_EMAIL] = email
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val username = data?.get(PARAM_USERNAME)
        val password = data?.get(PARAM_PASSWORD)
        val email = data?.get(PARAM_EMAIL)

        return if (username != null && password != null && email != null) {
            val attributes: HashMap<String, String> = HashMap()
            attributes["email"] = email as String
            userRepository.signUp(username as String, password as String, attributes)
        } else {
            Observable.error { IllegalArgumentException("username, password and email must be provided.") }
        }
    }
}