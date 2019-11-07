package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.PlanElementDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlanElementRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import java.sql.Timestamp


class PlanElementRepositoryTest {
    private val planElementRepository = spyk(PlanElementRepository())

    @Test
    fun `When getAllTravelsByUserId called on existing id expect list of PlanElementDao returned`() {
        val result = planElementRepository.getPlanElementsByTravelId(1)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `When getAllTravelsByUserId called on non existing id expect empty list returned`() {
        val result = planElementRepository.getPlanElementsByTravelId(-1)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `When get called on existing id expect one PlanElementDao returned`() {
        val result = planElementRepository.get(1)
        Assert.assertTrue(result is PlanElementDao)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = planElementRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`() {
        val result = planElementRepository.add(PlanElementDao(0, Timestamp(1111), 1, 1, ,""))
        Assert.assertTrue(result)
        planElementRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        planElementRepository.add(PlanElementDao(0, Timestamp(1111), 1, 1, false,""))
        val result = planElementRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = planElementRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing PlanElementDao expect true returned`() {
        val result = planElementRepository.update(PlanElementDao(1, Timestamp(1111), 1, 1, false,""))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing PlanElementDao expect false returned`() {
        val result = planElementRepository.update(PlanElementDao(0, Timestamp(1111), 1, 1, false,""))
        Assert.assertTrue(!result)
    }
}
