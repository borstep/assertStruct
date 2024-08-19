package ua.kiev.its.assertstruct.template.date;

import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.impl.factories.date.NowNode;
import ua.kiev.its.assertstruct.template.Template;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import static ua.kiev.its.assertstruct.TestUtils.*;

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
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC);
        checkFail("'$NOW'", "'" + dtf.format(notNow)+"'", notNow);
    }

}
