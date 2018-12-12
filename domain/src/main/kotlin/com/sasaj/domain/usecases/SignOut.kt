package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable


class SignOut(
        transformer: Transformer<Boolean>,
        private val userRepository: UserRepository) : UseCase<Boolean>(transformer) {

    fun signOut() : Observable<Boolean> {
        return observable()
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        return userRepository.signOut()
    }
}