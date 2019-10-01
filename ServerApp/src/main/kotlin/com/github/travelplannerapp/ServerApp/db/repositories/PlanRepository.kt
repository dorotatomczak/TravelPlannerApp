package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.datamodels.ObjectCategory
import com.github.travelplannerapp.ServerApp.datamodels.Place
import com.github.travelplannerapp.ServerApp.datamodels.Plan
import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository.Companion.tableName
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class PlanRepository : Repository<PlanDao>(), IPlanRepository {
    companion object {
        const val tableName = "plan"
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

    override fun getPlansByTravelId(travelId: Int): List<Plan> {
        val plans = mutableListOf<Plan>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT *" +
                                "FROM $tableName INNER JOIN ${PlaceRepository.tableName} " +
                                "on $tableName.$columnPlaceId = ${PlaceRepository.tableName}.${PlaceRepository.columnId} " +
                                "where $columnTravelId = ?"
                )
        statement.setInt(1, travelId)
        val result = statement.executeQuery()
        while (result.next()) {
            val planDao= PlanDao(result)
            val placeDao = PlaceDao(result)
            placeDao.id = result.getInt("place_id")

            val place = Place(
                    placeDao.hereId!!,
                    placeDao.title!!,
                    placeDao.vicinity!!,
                    emptyArray<Double>(),
                    ObjectCategory("", placeDao.category!!),
                    placeDao.href!!)
            val plan = Plan(planDao.id!!,
                    planDao.locale!!,
                    planDao.fromDateTime!!.time,
                    planDao.toDateTime!!.time,
                    planDao.placeId!!,
                    place)

            plans.add(plan)
        }
        return plans
    }

    override fun T(result: ResultSet): PlanDao? {
        return PlanDao(result)
    }

    override fun prepareInsertStatement(obj: PlanDao): PreparedStatement {
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

    override fun prepareUpdateStatement(obj: PlanDao): PreparedStatement {
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
