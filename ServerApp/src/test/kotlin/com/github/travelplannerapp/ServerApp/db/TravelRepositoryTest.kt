package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class TravelRepositoryTest {
    private val travelRepository = spyk(TravelRepository())

    @Test
    fun `When getAllTravelsByUserId called on existing id expect list of Travel returned`() {
        val result = travelRepository.getAllTravelsByUserId(1)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `When getAllTravelsByUserId called on non existing id expect empty list returned`() {
        val result = travelRepository.getAllTravelsByUserId(-1)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `When get called on existing id expect one Travel returned`() {
        val result = travelRepository.get(1)
        Assert.assertTrue(result is Travel)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = travelRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`() {
        val result = travelRepository.add(Travel(0,"name"))
        Assert.assertTrue(result)

        travelRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        travelRepository.add(Travel(0,"name"))

        val result = travelRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = travelRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing Travel expect true returned`(){
        val result = travelRepository.update(Travel(1,"travelName"))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing Travel expect false returned`(){
        val result = travelRepository.update(Travel(0,"travelName"))
        Assert.assertTrue(!result)
    }
}
