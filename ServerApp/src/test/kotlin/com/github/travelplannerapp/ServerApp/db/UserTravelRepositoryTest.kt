package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class UserTravelRepositoryTest {
    private val userTravelRepository = spyk(UserTravelRepository())

    @Test
    fun `When deleteUserTravelBinding called with existing parameters expect false returned`() {
        val userRepository = UserRepository()
        val travelRepository = TravelRepository()
        userRepository.add(User(-1,"email@email","password"))
        travelRepository.add(Travel(-1,"travelName"))

        val result = userTravelRepository.deleteUserTravelBinding(-1,-1)
        Assert.assertTrue(!result)

        userRepository.delete(-1)
        travelRepository.delete(-1)
    }

    @Test
    fun `When deleteUserTravelBinding called with non existing parameters expect false returned`() {
        val result = userTravelRepository.deleteUserTravelBinding(-1,-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When countByTravelId called on existing UserTravel with travel id expect more than 0 returned`() {
        val result = userTravelRepository.countByTravelId(1)
        Assert.assertTrue(result > 0)
    }

    @Test
    fun `When countByTravelId called on non existing UserTravel with travel id expect 0 returned`() {
        val result = userTravelRepository.countByTravelId(-1)
        Assert.assertTrue(result == 0)
    }

    @Test
    fun `When get called on existing id expect one UserTravel returned`() {
        val result = userTravelRepository.get(1)
        Assert.assertTrue(result is UserTravel)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = userTravelRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`() {
        val result = userTravelRepository.add(UserTravel(0,1,1))
        Assert.assertTrue(result)
        userTravelRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        userTravelRepository.add(UserTravel(0,1,1))
        val result = userTravelRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = userTravelRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing UserTravel expect true returned`(){
        val result = userTravelRepository.update(UserTravel(1,1,1))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing UserTravel expect false returned`(){
        val result = userTravelRepository.update(UserTravel(0,0,0))
        Assert.assertTrue(!result)
    }
}
