package org.assertstruct.template.date;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertstruct.TestUtils.*;

class AnyDateTest {

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
    void anyDateTime_sqlDateOK() throws IOException {
        checkOK(
                "'$ANY_DATE'",
                new java.sql.Date(System.currentTimeMillis())
        );
    }

    @Test
    void anyTime_StringOK() throws IOException {
        checkOK(
                "'$ANY_TIME'",
                "10:20:30"
        );
    }

}