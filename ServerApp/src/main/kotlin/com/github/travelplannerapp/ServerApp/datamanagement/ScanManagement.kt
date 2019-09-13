package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Scan
import com.github.travelplannerapp.ServerApp.db.repositories.ScanRepository
import com.github.travelplannerapp.ServerApp.exceptions.AddScanException
import com.github.travelplannerapp.ServerApp.exceptions.DeleteScansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ScanManagement : IScanManagement {

    @Autowired
    lateinit var scanRepository: ScanRepository

    override fun addScan(userId: Int, travelId: Int, name: String): Scan {
        val scanId = scanRepository.getNextId()
        val newScan = Scan(scanId, userId, travelId, name)
        if (!scanRepository.add(newScan)) {
            throw AddScanException("Error when adding scan")
        } else return newScan
    }

    override fun getScans(userId: Int, travelId: Int): List<Scan> {
        return scanRepository.getAll(userId, travelId)
    }

    override fun deleteScan(scan: Scan) {
        val result = scanRepository.delete(scan.id!!)
        if (!result) throw DeleteScansException("Error when deleting scans")
    }
}
