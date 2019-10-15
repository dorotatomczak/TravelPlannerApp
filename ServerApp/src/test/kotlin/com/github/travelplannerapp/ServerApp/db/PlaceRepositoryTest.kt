package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class PlaceRepositoryTest {
    private val placeRepository = spyk(PlaceRepository())

    @Test
    fun `When getPlaceByHereId called with existing hereId expect one PlaceDao returned`() {
        val result = placeRepository.getPlaceByHereId("hereId")
        Assert.assertTrue(result is PlaceDao)
    }

    @Test
    fun `When getPlaceByHereId called with non existing hereId expect null returned`() {
        val result = placeRepository.getPlaceByHereId("noHereId")
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When get called on existing id expect one PlaceDao returned`() {
        val result = placeRepository.get(1)
        Assert.assertTrue(result is PlaceDao)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = placeRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`(){
        val result = placeRepository.add(PlaceDao(0,"hereId","href","title","vicinity",0))
        Assert.assertTrue(result)
        placeRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        placeRepository.add(PlaceDao(0,"hereId","href","title","vicinity",0))
        val result = placeRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = placeRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing PlaceDao expect true returned`(){
        val result = placeRepository.update(PlaceDao(1,"hereId","href","title","vicinity",0))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing PlaceDao expect false returned`(){
        val result = placeRepository.update(PlaceDao(0,"hereId","href","title","vicinity",0))
        Assert.assertTrue(!result)
    }
}
