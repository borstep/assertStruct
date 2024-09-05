package org.assertstruct.template.date;

import org.assertstruct.Res;
import org.assertstruct.impl.factories.date.AnyDateNode;
import org.assertstruct.template.Template;
import org.assertstruct.template.TemplateParseException;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class DateNodeTest {
    @Test
    void parse_OK() {
        Template template = Res.res("'$DATE(uuuu-MM-dd)'").asTemplate();
        assertInstanceOf(AnyDateNode.class,
                template.getRoot());
        assertEquals(template.getRoot().getToken().getValue(), "$DATE(uuuu-MM-dd)");
    }
    @Test
    void parse_Fail() {
        TemplateParseException templateParseException = assertThrows(TemplateParseException.class, () -> {
            Res.res("'$DATE(ttttt)'").asTemplate();
        });

        assertEquals("Unrecognized date format: ttttt", templateParseException.getMessage());
    }

    @Test
    void assert_OK() {
        checkOK("'$DATE(uuuu-MM-dd)'", "2020-01-20");
    }

    @Test
    void assert_Fail() {
        checkFail("'$DATE(uuuu-MM-dd)'", "'2020-20-10'","2020-20-10");
    }

}
