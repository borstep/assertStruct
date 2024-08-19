package ua.kiev.its.assertstruct.template.date;

import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.impl.factories.date.AnyDateNode;
import ua.kiev.its.assertstruct.template.Template;
import ua.kiev.its.assertstruct.template.TemplateParseException;

import static org.junit.jupiter.api.Assertions.*;
import static ua.kiev.its.assertstruct.TestUtils.*;

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
            Template template = Res.res("'$DATE(ttttt)'").asTemplate();
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
