package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import java.util.HashMap

class ChangePassword(
        transformer: Transformer<Boolean>,
        private val userRepository: UserRepository) : UseCase<Boolean>(transformer) {

    companion object {
        private const val PARAM_USERNAME = "param:username"
    }

    fun changePassword(username : String) : Observable<Boolean>{
        val data = HashMap<String, String>()
        data[PARAM_USERNAME] = username
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val username = data?.get(PARAM_USERNAME)
        username?.let {
            return userRepository.changePassword(username as String)
        } ?: return Observable.error { IllegalArgumentException("Username must be provided.") }
    }
}