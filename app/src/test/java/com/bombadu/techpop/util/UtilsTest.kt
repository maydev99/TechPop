package com.bombadu.techpop.util


import com.google.common.truth.Truth.assertThat
import io.grpc.okhttp.internal.Util
import org.checkerframework.dataflow.qual.TerminatesExecution
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith


class UtilsTest {

    @Test
    fun convertDaysToTimeStamp_ConvertToEpochTime_Valid() {
        val utils = Utils
        val days = 2
        val expectedResult: Long = 172800
        val result = utils.convertDaysToTimestampTime(days)
        assertEquals(result, expectedResult)

    }


    @Test
    fun convertDaysToTimeStamp_ConvertToEpochTime_False() {
        val utils = Utils
        val days = 1
        val expectedResult: Long = 172800
        val result = utils.convertDaysToTimestampTime(days)
        assertNotEquals(result, expectedResult)

    }

    @Test
    fun convertServerDateToDate_True(){
        val utils = Utils
        val serverDate = "2021-09-17T18:07:22.7634507Z"
        val expectedResult ="Fri, 9.17.2021"
        val result = utils.convertServerDateToDate(serverDate)
        assertEquals(result, expectedResult)
    }






}
