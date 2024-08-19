package ua.kiev.its.assertstruct.template.date;

import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.impl.factories.date.AnyDateNode;
import ua.kiev.its.assertstruct.impl.factories.date.NowNode;
import ua.kiev.its.assertstruct.template.Template;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static ua.kiev.its.assertstruct.TestUtils.*;

class AnyDateTimeTest {
    @Test
    void parse_OK() {
        Template template = Res.res("'$ANY_DATETIME'").asTemplate();
        assertInstanceOf(AnyDateNode.class,
                template.getRoot());
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