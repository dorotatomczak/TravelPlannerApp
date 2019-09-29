package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlanElement
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class PlanElementRepository : Repository<PlanElement>(), IPlanElementRepository {
    companion object {
        const val tableName = "plan_element"
        const val columnId = "id"
        const val columnLocale = "locale"
        const val columnFromDateTime = "from_date_time"
        const val columnToDateTime = "to_date_time"
        const val columnPlaceId = "place_id"
        const val columnTravelId = "travel_id"
    }

    override val insertStatement = "INSERT INTO $tableName " +
            "($columnId, $columnLocale, $columnFromDateTime, $columnToDateTime, $columnPlaceId, $columnTravelId) " +
            "VALUES (?, ?, ?, ?, ?, ?) "
    override val selectStatement = "SELECT * FROM $tableName "
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName " +
            "SET $columnLocale=?, $columnFromDateTime=?, $columnToDateTime=?, $columnPlaceId=?, $columnTravelId=? " +
            "WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun T(result: ResultSet): PlanElement? {
        return PlanElement(result)
    }

    override fun prepareInsertStatement(obj: PlanElement): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setString(2, obj.locale)
        statement.setTimestamp(3, obj.fromDateTime!!)
        statement.setTimestamp(4, obj.toDateTime)
        statement.setInt(5, obj.placeId!!)
        statement.setInt(6, obj.travelId!!)
        return statement
    }

    override fun prepareUpdateStatement(obj: PlanElement): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.locale)
        statement.setTimestamp(2, obj.fromDateTime!!)
        statement.setTimestamp(3, obj.toDateTime)
        statement.setInt(4, obj.placeId!!)
        statement.setInt(5, obj.travelId!!)
        statement.setInt(6, obj.id!!)
        return statement
    }
}
