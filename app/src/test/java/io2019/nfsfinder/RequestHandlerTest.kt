package io2019.nfsfinder

class RequestHandlerTest {
    /*private val requestHandler = RequestHandler()

    @Test
    fun requestLoginTest() {
        val test = LoggedInUser(3, "unit")
        val response = requestHandler.requestLogin("unit@test.io", "test")
        assertEquals(test, response)
    }*/

    /* fun locOpTestTemplate(lng: Double, lat: Double) {
        val userId = 3
        val res = requestHandler.updateLocation(userId, lng, lat)
        assertEquals(1, res)

        val resPair = requestHandler.requestLocation(userId)
        assertEquals(Pair(lng, lat), resPair)
    }*/

    /*fun locOpTestExcTemplate(lng: Double, lat: Double) {
        var thrown = false

        try {
            locOpTestTemplate(lng, lat)
        } catch (e: MysqlDataTruncation) {
            thrown = true
        }

        assertTrue(thrown)
    }

    @Test
    fun locOpTest1() {
        locOpTestTemplate(20.0, 30.0)
    }

    @Test
    fun locOpTest2() {
        locOpTestTemplate(0.0, 0.0)
    }

    @Test
    fun locOpTest3() {
        locOpTestExcTemplate(181.0, 0.0)
    }

    @Test
    fun locOpTest4() {
        locOpTestExcTemplate(0.0, 91.0)
    }

    @Test
    fun locOpTest5() {
        locOpTestExcTemplate(-181.0, 0.0)
    }

    @Test
    fun locOpTest6() {
        locOpTestExcTemplate(0.0, -91.0)
    }*/
}