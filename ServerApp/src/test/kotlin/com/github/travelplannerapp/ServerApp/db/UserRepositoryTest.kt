package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import io.mockk.spyk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test

class UserRepositoryTest {

    @Test
    fun whenGetCalledOnExistingIdExpectOneUserReturned() {
        val userRepository = spyk(UserRepository())

        val result = userRepository.get(18)

        verify { userRepository.get(18) }
        Assert.assertTrue(result is User)
    }

    @Test
    fun whenGetCalledOnNonExistingIdExpectNullReturned() {
        val userRepository = spyk(UserRepository())

        val result = userRepository.get(0)

        verify { userRepository.get(0) }
        assertEquals(null, result)
    }
}