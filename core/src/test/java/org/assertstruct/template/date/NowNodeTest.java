package org.assertstruct.template.date;

import org.assertstruct.Res;
import org.assertstruct.impl.factories.date.NowNode;
import org.assertstruct.template.Template;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.*;
import static org.assertstruct.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class NowNodeTest {
    @Test
    void parse_OK() {
        Template template = Res.res("'$NOW'").asTemplate();
        assertInstanceOf(NowNode.class,
                template.getRoot());
        assertEquals( ((NowNode)template.getRoot()).getPrecision(), 60*1000);
    }

    @Test
    void assert_OK() {
        checkOK("'$NOW'", Instant.now().minus(30, SECONDS));
    }

    @Test
    void assert_Fail() {
        Instant notNow = Instant.now().minus(900, SECONDS);
//        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC);
        checkFail("'$NOW'", "'" + notNow.toString()+"'", notNow);
    }

}
