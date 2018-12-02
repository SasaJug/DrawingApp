package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.HashMap

class VerifyUser(
        transformer: Transformer<Boolean>,
        private val userRepository: UserRepository) : UseCase<Boolean>(transformer) {


    companion object {
        private const val PARAM_USERNAME = "param:username"
        private const val PARAM_CODE = "param:code"
    }

    fun verifyUser(username: String, code: String): Observable<Boolean> {
        val data = HashMap<String, String>()
        data[PARAM_USERNAME] = username
        data[PARAM_CODE] = code
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val username = data?.get(PARAM_USERNAME)
        val code = data?.get(PARAM_CODE)

        return if (username != null && code != null) {
            userRepository.verify(username as String, code as String)
        } else {
            Observable.error { IllegalArgumentException("username and code must be provided.") }
        }
    }
}