package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Scan
import com.github.travelplannerapp.ServerApp.db.repositories.ScanRepository
import com.github.travelplannerapp.ServerApp.exceptions.AddScanException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ScanManagement : IScanManagement {

    @Autowired
    lateinit var scanRepository: ScanRepository

    override fun addScan(userId: Int, travelId: Int, name: String) {
        val scanId = scanRepository.getNextId()
        val newScan = Scan(scanId, userId, travelId, name)
        if (!scanRepository.add(newScan)){
            throw AddScanException("Error when adding scan")
        }
    }

    fun getScanNames(userId: Int, travelId: Int): List<String> {
        val scans = scanRepository.getAll(userId, travelId)
        return scans.map { it.name!! }
    }
}