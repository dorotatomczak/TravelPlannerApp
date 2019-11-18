package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.Scan
import com.github.travelplannerapp.ServerApp.db.repositories.ScanRepository
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class ScanRepositoryTest {
    private val scanRepository = spyk(ScanRepository())

    @Test
    fun `When getAll called with right parameters expect list of Scan returned`() {
        val result = scanRepository.getAll(1,1)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `When getAll called with wrong parameters expect empty list returned`() {
        val result = scanRepository.getAll(-1,-1)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `When get called on existing id expect one Scan returned`() {
        val result = scanRepository.get(1)
        Assert.assertTrue(result is Scan)
    }

    @Test
    fun `When get called on non existing id expect null returned`() {
        val result = scanRepository.get(-1)
        Assert.assertTrue(result == null)
    }

    @Test
    fun `When add called expect one row returned`() {
        val result = scanRepository.add(Scan(0,1,1,"scanName"))
        Assert.assertTrue(result)

        scanRepository.delete(0)
    }

    @Test
    fun `When delete called on existing id expect true returned`() {
        scanRepository.add(Scan(0,1,1,"scanName"))

        val result = scanRepository.delete(0)
        Assert.assertTrue(result)
    }

    @Test
    fun `When delete called on non existing id expect false returned`() {
        val result = scanRepository.delete(-1)
        Assert.assertTrue(!result)
    }

    @Test
    fun `When update called on existing Scan expect true returned`(){
        val result = scanRepository.update(Scan(1,1,1,"scanName"))
        Assert.assertTrue(result)
    }

    @Test
    fun `When update called on non existing Scan expect false returned`(){
        val result = scanRepository.update(Scan(0,1,1,"scanName"))
        Assert.assertTrue(!result)
    }
}
