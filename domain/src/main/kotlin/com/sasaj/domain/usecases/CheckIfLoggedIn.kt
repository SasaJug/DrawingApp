package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable

class CheckIfLoggedIn(
        transformer: Transformer<String>,
        private val userRepository: UserRepository) : UseCase<String>(transformer) {

    fun checkIfLoggedIn(): Observable<String> {
        return observable()
    }

    override fun createObservable(data: Map<String, Any>?): Observable<String> {
            return userRepository.checkIfLoggedIn()
    }
}