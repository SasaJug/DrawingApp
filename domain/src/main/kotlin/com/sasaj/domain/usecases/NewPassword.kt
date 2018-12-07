package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import java.util.*

class NewPassword(transformer: Transformer<Boolean>,
                  private val userRepository: UserRepository) : UseCase<Boolean>(transformer) {

    companion object {
        private const val PARAM_NEW_PASSWORD = "param:password"
        private const val PARAM_CODE = "param:code"
    }

    fun newPassword(newPassword: String, code: String): Observable<Boolean> {
        val data = HashMap<String, String>()
        data[PARAM_NEW_PASSWORD] = newPassword
        data[PARAM_CODE] = code
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val newPassword = data?.get(PARAM_NEW_PASSWORD)
        val code = data?.get(PARAM_CODE)
        return if (newPassword != null && code != null) {
            userRepository.newPassword(newPassword as String, code as String)
        } else Observable.error { IllegalArgumentException("New password and code must be provided.") }
    }
}