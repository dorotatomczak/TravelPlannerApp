package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class UserRepositoryTest {

    @Test
    fun whenGetCalledOnExistingIdExpectOneUserReturned() {
        val userRepository = spyk(UserRepository())

        val result = userRepository.get(1)

        verify { userRepository.get(1) }
        Assert.assertTrue(result is User)
    }

    @Test
    fun whenGetCalledOnNonExistingIdExpectNullReturned() {
        val userRepository = spyk(UserRepository())

        val result = userRepository.get(0)

        verify { userRepository.get(0) }
        Assert.assertTrue(result == null)
    }
}