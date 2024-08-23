package org.assertstruct.template.date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertstruct.AssertStruct;
import org.assertstruct.impl.converter.jackson.JacksonConverter;
import org.junit.jupiter.api.Test;
import org.assertstruct.Res;
import org.assertstruct.impl.factories.date.NowNode;
import org.assertstruct.template.Template;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertstruct.TestUtils.*;

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
    void assert_Fail() throws JsonProcessingException {
        Instant notNow = Instant.now().minus(900, SECONDS);
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC);
        checkFail("'$NOW'", "'" + dtf.format(notNow)+"'", notNow);
    }

}
