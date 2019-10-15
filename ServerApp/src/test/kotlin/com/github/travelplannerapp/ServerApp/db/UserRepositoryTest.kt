package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class UserRepositoryTest {
    private val userRepository = spyk(UserRepository())

    @Test
    fun `When get called on existing id expect one User returned`() {
        val result = userRepository.get(1)
        Assert.assertTrue(result is User)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = userRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`(){
        val result = userRepository.add(User(0,"email@email.com","password"))
        Assert.assertTrue(result)
        userRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        userRepository.add(User(0,"email@email.com","password"))
        val result = userRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = userRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing User expect true returned`(){
        val result = userRepository.update(User(1,"email@email","password"))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing User expect false returned`(){
        val result = userRepository.update(User(0,"email","password"))
        Assert.assertTrue(!result)
    }
}
