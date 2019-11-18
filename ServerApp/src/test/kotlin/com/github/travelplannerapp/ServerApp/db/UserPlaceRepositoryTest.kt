package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.UserPlace
import com.github.travelplannerapp.ServerApp.db.repositories.UserPlaceRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class UserPlaceRepositoryTest {

    private val userPlaceRepository = spyk(UserPlaceRepository())

    private val existingUserPlace = UserPlace(1,1,1,5)
    private val newUserPlace = UserPlace(0, 1,1,5)

    @Test
    fun `When getUserPlaceByUserIdAndPlaceId called with non existing parameters expect null returned`() {
        val result = userPlaceRepository.getUserPlaceByUserIdAndPlaceId(-1, -1)
        Assert.assertNull(result)
    }

    @Test
    fun `When getUserPlaceByUserIdAndPlaceId called with existing parameters expect UserPlace object returned`() {
        val result = userPlaceRepository.getUserPlaceByUserIdAndPlaceId(1, 1)
        Assert.assertTrue(result is UserPlace)
        Assert.assertEquals(5, (result as UserPlace).rating)
    }

    @Test
    fun `When get called on existing id expect one UserTravel returned`() {
        val result = userPlaceRepository.get(1)
        Assert.assertTrue(result is UserPlace)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = userPlaceRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`() {
        val result = userPlaceRepository.add(newUserPlace)
        Assert.assertTrue(result)
        userPlaceRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        userPlaceRepository.add(UserPlace(0,1,1,5))
        val result = userPlaceRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = userPlaceRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing UserTravel expect true returned`(){
        val result = userPlaceRepository.update(existingUserPlace)
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing UserTravel expect false returned`(){
        val result = userPlaceRepository.update(newUserPlace)
        Assert.assertTrue(!result)
    }
}
