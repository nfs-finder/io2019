package io2019.nfsfinder.data.database

import java.util.Properties
import java.sql.*

//TODO COMMENTS

class DatabaseHandler {
    private var conn: Connection? = null
    private val username = "app_handler" // provide the username
    private val password = "nfsf!nd3r" // provide the corresponding password
    private val endpoint = "nfsfinderdb.c9gtwnwynhda.us-east-2.rds.amazonaws.com"

    private fun getConnection() {
        if (conn != null)
            return

        val connectionProps = Properties()
        connectionProps["user"] = username
        connectionProps["password"] = password

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
        conn = DriverManager.getConnection(
                "jdbc:mysql://$endpoint:3306/nfsfinderdb",
                connectionProps)
    }

    fun closeConnection() {
        conn?.close()
        conn = null
    }

    fun executeQuery(query: String) : ResultSet {
        getConnection()
        val stmt = conn!!.createStatement()
        return stmt.executeQuery(query)
    }

    fun executeUpdate(query: String) : Int {
        getConnection()
        val stmt = conn!!.createStatement()
        return stmt.executeUpdate(query)
    }
}