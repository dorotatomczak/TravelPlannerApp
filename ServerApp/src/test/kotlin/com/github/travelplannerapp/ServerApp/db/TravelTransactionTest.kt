package com.github.travelplannerapp.ServerApp.db

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import com.github.travelplannerapp.ServerApp.db.transactions.TravelTransaction
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class TravelTransactionTest {
    private val travelTransaction = spyk(TravelTransaction())
    private val travelId = 1
    private val travelName = "travelName"
    private val userTravelId = 1
    private val userId = 1

    @Test
    fun whenAddCalledWithExistingUserIdExpectCommitAndTravelReturned() {
        travelTransaction.travelRepository = mockkClass(TravelRepository::class)
        travelTransaction.userTravelRepository = mockkClass(UserTravelRepository::class)

        travelTransaction.travel = Travel(travelId, travelName)
        travelTransaction.userTravel = UserTravel(userTravelId, userId, travelId)

        every { travelTransaction.travelRepository.getNextId() } returns travelId
        every { travelTransaction.userTravelRepository.getNextId() } returns userTravelId
        every { travelTransaction.travelRepository.add(travelTransaction.travel) } returns true
        every { travelTransaction.userTravelRepository.add(travelTransaction.userTravel) } returns true

        val result = travelTransaction.addTravel(travelName, userId)
        Assert.assertTrue(DbConnection.conn.autoCommit)
        Assert.assertTrue(result is Travel)
    }

    @Test
    fun whenAddCalledWithAddUserTravelErrorExpectRollbackAndNullReturned() {
        travelTransaction.travelRepository = mockkClass(TravelRepository::class)
        travelTransaction.userTravelRepository = mockkClass(UserTravelRepository::class)

        travelTransaction.travel = Travel(travelId, travelName)
        travelTransaction.userTravel = UserTravel(userTravelId, userId, travelId)

        every { travelTransaction.travelRepository.getNextId() } returns travelId
        every { travelTransaction.userTravelRepository.getNextId() } returns userTravelId
        every { travelTransaction.travelRepository.add(travelTransaction.travel ) } returns true
        every { travelTransaction.userTravelRepository.add(travelTransaction.userTravel) } returns false

        val result = travelTransaction.addTravel(travelName, userId)

        Assert.assertTrue(DbConnection.conn.autoCommit)
        Assert.assertTrue(result == null)
    }

    @Test
    fun whenAddCalledWithAddTravelErrorExpectRollbackAndNullReturned() {
        travelTransaction.travelRepository = mockkClass(TravelRepository::class)
        travelTransaction.userTravelRepository = mockkClass(UserTravelRepository::class)

        travelTransaction.travel = Travel(travelId, travelName)
        travelTransaction.userTravel = UserTravel(userTravelId, userId, travelId)

        every { travelTransaction.travelRepository.getNextId() } returns travelId
        every { travelTransaction.userTravelRepository.getNextId() } returns userTravelId
        every { travelTransaction.travelRepository.add(travelTransaction.travel) } returns false
        every { travelTransaction.userTravelRepository.add(travelTransaction.userTravel) } returns true

        val result = travelTransaction.addTravel(travelName, userId)
        Assert.assertTrue(DbConnection.conn.autoCommit)
        Assert.assertTrue(result == null)
    }
}
