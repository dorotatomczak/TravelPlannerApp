package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class UserRepository : Repository<User>(), IUserRepository {
    companion object {
        const val tableName = "app_user"
        const val columnId = "id"
        const val columnEmail = "email"
        const val columnPassword = "password"
        const val columnAuthToken = "authtoken"
        const val columnExpirationDate = "expirationdate"

        private const val insertStatement = "INSERT INTO $tableName " +
                "($columnId,$columnEmail,$columnPassword,$columnAuthToken,$columnExpirationDate) " +
                "VALUES (?,?,?,?,?) "
        private const val selectStatement = "SELECT * FROM $tableName "
        private const val deleteStatement = "DELETE FROM $tableName "
        private const val updateStatement = "UPDATE $tableName " +
                "SET $columnEmail=?, $columnPassword=?, $columnAuthToken=?, $columnExpirationDate=?" +
                " WHERE $columnId=?"
        private const val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"
    }

    override fun getUserByEmail(email: String): User? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnEmail=?")
        statement.setString(1, email)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return User(result)
        }
        return null
    }

    override fun T(result: ResultSet): User? {
        return User(result)
    }

    override fun prepareSelectByIdStatement(id: Int): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        return statement
    }

    override fun prepareSelectAllStatement(): PreparedStatement {
        return DbConnection
                .conn
                .prepareStatement(selectStatement)
    }

    override fun prepareInsertStatement(obj: User): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1,obj.id!!)
        statement.setString(2, obj.email)
        statement.setString(3, obj.password)
        statement.setString(4, obj.token)
        statement.setTimestamp(5, obj.expirationDate)
        return statement
    }

    override fun prepareUpdateStatement(obj: User): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.email)
        statement.setString(2, obj.password)
        statement.setString(3, obj.token)
        statement.setTimestamp(4, obj.expirationDate)
        statement.setInt(5,obj.id!!)
        return statement
    }

    override fun prepareDeleteByIdStatement(id: Int): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        return statement
    }

    override fun prepareDeleteAllStatement(): PreparedStatement {
        return DbConnection
                .conn
                .prepareStatement(deleteStatement)
    }

    override fun prepareNextIdStatement(): PreparedStatement {
        return  DbConnection.conn
                .prepareStatement(nextIdStatement)
    }
}