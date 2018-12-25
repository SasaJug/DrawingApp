package com.sasaj.domain.usecases

import com.sasaj.domain.UserRepository
import com.sasaj.domain.common.TestTransformer
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations

class ChangePasswordUseCaseTest {

    var changePasswordUseCase :ChangePasswordUseCase? = null

    @Mock
    var userRepository : UserRepository? = null


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        changePasswordUseCase = ChangePasswordUseCase(TestTransformer(), userRepository!!)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun changePassword() {
        doReturn(Observable.just(true)).`when`(userRepository)!!.changePassword("Sasa")
        changePasswordUseCase?.changePassword("Sasa")!!.test()
                .assertComplete()
    }


    @Test
    fun changePasswordNoUsername() {
//        doReturn(Observable.just(true)).`when`(userRepository)!!.changePassword("")
        changePasswordUseCase?.changePassword("")!!.test()
                .assertError(Throwable("Username must be provided."))
    }
}