package ua.kiev.its.assertstruct.template;

import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.utils.MapUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static ua.kiev.its.assertstruct.TestUtils.*;

class TemplateAnyDateTest {

    @Test
    void anyDate_String_OK() throws IOException {
        checkOK(
                "'$ANY_DATE'",
                "2021-01-20"
        );
    }
    @Test
    void anyDate_String_WrongMonth_Fail() throws IOException {
        checkFail(
                "'$ANY_DATE'",
                "'2021-20-01'",
                "2021-20-01"
        );
    }
    @Test
    void anyDate_LocalDate_OK() throws IOException {
        checkOK(
                "'$ANY_DATE'",
                LocalDate.of(2021, 10, 20)
        );
    }

    @Test
    void anyDateTime_DateOK() throws IOException {
        checkOK(
                "'$ANY_DATETIME'",
                new Date()
        );
    }

    @Test
    void anyDateTime_sqlDateOK() throws IOException {
        checkOK(
                "'$ANY_DATE'",
                new java.sql.Date(System.currentTimeMillis())
        );
    }

    @Test
    void anyDateTime_sqlTimestampOK() throws IOException {
        checkOK(
                "'$ANY_DATETIME'",
                new java.sql.Timestamp(System.currentTimeMillis())
        );
    }

    @Test
    void anyTime_StringOK() throws IOException {
        checkOK(
                "'$ANY_TIME'",
                "10:20:30"
        );
    }

    @Test
    void anyTime_LocalTime_OK() throws IOException {
        checkOK(
                "'$ANY_TIME'",
                LocalTime.of(10, 20, 30)
        );
    }

    @Test
    void anyTime_LocalTime_WithNanos_OK() throws IOException {
        checkOK(
                "'$ANY_TIME'",
                LocalTime.of(10, 20, 30,5)
        );
    }
    @Test
    void anyTime_LocalTime_WithMillis_OK() throws IOException {
        checkOK(
                "'$ANY_TIME'",
                LocalTime.of(10, 20, 30,5*1000*1000)
        );
    }

    @Test
    void anyTime_String_Fail() throws IOException {
        checkFail(
                "'$ANY_TIME'",
                "'30:20:30'",
                "30:20:30"
        );
    }


}