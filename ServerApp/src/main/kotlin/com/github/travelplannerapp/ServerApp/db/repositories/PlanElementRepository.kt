package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanElementDao
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class PlanElementRepository : Repository<PlanElementDao>(), IPlanElementRepository {
    companion object {
        const val tableName = "plan_element"
        const val columnId = "id"
        const val columnFromDateTime = "from_date_time"
        const val columnPlaceId = "place_id"
        const val columnTravelId = "travel_id"
        const val columnCompleted = "completed"
       const val columnNotes = "notes"
    }

    override val insertStatement = "INSERT INTO $tableName " +
            "($columnId, $columnFromDateTime, $columnPlaceId, $columnTravelId,$columnCompleted, $columnNotes) " +
            "VALUES (?, ?, ?, ?,?,?) "
    override val selectStatement = "SELECT * FROM $tableName "
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName " +
            "SET $columnFromDateTime=?, $columnPlaceId=?, $columnTravelId=?, $columnCompleted=?, $columnNotes=? " +
            "WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun getPlanElementsByTravelId(travelId: Int): List<Pair<PlanElementDao, PlaceDao>> {
        val planElementDaoPlaceDao = mutableListOf<Pair<PlanElementDao, PlaceDao>>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT *" +
                                "FROM $tableName INNER JOIN ${PlaceRepository.tableName} " +
                                "ON $tableName.$columnPlaceId = ${PlaceRepository.tableName}.${PlaceRepository.columnId} " +
                                "WHERE $columnTravelId = ? ORDER BY $columnFromDateTime ASC"
                )
        statement.setInt(1, travelId)
        val result = statement.executeQuery()
        while (result.next()) {
            val planElementDao = PlanElementDao(result)
            val placeDao = PlaceDao(result)
            placeDao.id = result.getInt(columnPlaceId)
            planElementDaoPlaceDao.add(Pair(planElementDao, placeDao))
        }
        return planElementDaoPlaceDao
    }

    override fun deletePlanElementsByTravelId(travelId: Int): Boolean {
        val statement = DbConnection
            .conn
            .prepareStatement(deleteStatement + "WHERE $columnTravelId=?")
        statement.setInt(1, travelId)
        return statement.executeUpdate() > 0
    }

    override fun T(result: ResultSet): PlanElementDao? {
        return PlanElementDao(result)
    }

    override fun prepareInsertStatement(obj: PlanElementDao): PreparedStatement {
        val statement = DbConnection
            .conn
            .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setTimestamp(2, obj.fromDateTime!!)
        statement.setInt(3, obj.placeId!!)
        statement.setInt(4, obj.travelId!!)
        statement.setBoolean(5, obj.completed!!)
        statement.setString(6, obj.notes!!)
        return statement
    }

    override fun prepareUpdateStatement(obj: PlanElementDao): PreparedStatement {
        val statement = DbConnection
            .conn
            .prepareStatement(updateStatement)
        statement.setTimestamp(1, obj.fromDateTime!!)
        statement.setInt(2, obj.placeId!!)
        statement.setInt(3, obj.travelId!!)
        statement.setBoolean(4, obj.completed!!)
        statement.setString(5, obj.notes!!)
        statement.setInt(6, obj.id!!)
        return statement
    }
}
