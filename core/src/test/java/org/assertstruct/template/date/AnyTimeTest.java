package org.assertstruct.template.date;

import org.assertstruct.Res;
import org.assertstruct.impl.factories.date.AnyDateNode;
import org.assertstruct.template.Template;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertstruct.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class AnyTimeTest {
    @Test
    void parse_OK() {
        Template template = Res.res("'$ANY_TIME'").asTemplate();
        assertInstanceOf(AnyDateNode.class,
                template.getRoot());
    }

    @Test
    void anyTime_StringOK() {
        checkOK(
                "'$ANY_TIME'",
                "10:20:30"
        );
    }

    @Test
    void anyTime_LocalTime_OK() {
        checkOK(
                "'$ANY_TIME'",
                LocalTime.of(10, 20, 30)
        );
    }

    @Test
    void anyTime_LocalTime_WithNanos_OK() {
        checkOK(
                "'$ANY_TIME'",
                LocalTime.of(10, 20, 30, 5)
        );
    }

    @Test
    void anyTime_LocalTime_WithMillis_OK() {
        checkOK(
                "'$ANY_TIME'",
                LocalTime.of(10, 20, 30, 5 * 1000 * 1000)
        );
    }

    @Test
    void anyTime_String_Fail() {
        checkFail(
                "'$ANY_TIME'",
                "'30:20:30'",
                "30:20:30"
        );
    }
}