package io2019.nfsfinder

import io2019.nfsfinder.data.database.DatabaseHandler
import org.junit.Test

import org.junit.Assert.*

/**
 * Database handler unit tests.
 */
class DBHandlerTest {
    private val dbHandler = DatabaseHandler()

    @Test
    fun checkExistence() {
        val query = "SELECT * FROM users WHERE username='unit';"
        val res = dbHandler.executeQuery(query)
        assertTrue(res.next())
        assertEquals("unit@test.io", res.getString("email"))
        assertEquals("unit", res.getString("username"))
        assertEquals("test", res.getString("password"))
    }

    @Test
    fun changeUsernameTest() {
        var query = "UPDATE users SET username='units' WHERE username='unit';"
        var res = dbHandler.executeUpdate(query)
        assertEquals(1, res)

        query = "SELECT username FROM users WHERE username='units'"
        var resSet = dbHandler.executeQuery(query)
        assertTrue(resSet.next())
        assertEquals("units", resSet.getString("username"))

        dbHandler.closeConnection()

        query = "UPDATE users SET username='unit' WHERE username='units';"
        res = dbHandler.executeUpdate(query)
        assertEquals(1, res)

        query = "SELECT username FROM users WHERE username='unit'"
        resSet = dbHandler.executeQuery(query)
        assertTrue(resSet.next())
        assertEquals("unit", resSet.getString("username"))
    }
}
