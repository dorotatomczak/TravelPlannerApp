package com.github.travelplannerapp.ServerApp.db


import com.github.travelplannerapp.ServerApp.db.dao.UserFriend
import com.github.travelplannerapp.ServerApp.db.repositories.UserFriendRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class UserFriendRepositoryTest {
    private val userFriendRepository = spyk(UserFriendRepository())

    @Test
    fun `When get called on existing id expect one UserFriend returned`() {
        val result = userFriendRepository.get(1)
        Assert.assertTrue(result is UserFriend)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = userFriendRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`(){
        val result = userFriendRepository.add(UserFriend(0,1,1))
        Assert.assertTrue(result)
        userFriendRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        userFriendRepository.add(UserFriend(0,1,1))
        val result = userFriendRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = userFriendRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing UserFriend expect true returned`(){
        val result = userFriendRepository.update(UserFriend(1,1,1))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing UserFriend expect false returned`(){
        val result = userFriendRepository.update(UserFriend(0,1,1))
        Assert.assertTrue(!result)
    }
}
