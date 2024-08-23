import org.junit.jupiter.api.Test;
import org.assertstruct.Res;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertstruct.AssertStruct.assertStruct;

public class ResTest {
    Res POJO = Res.from("example/pojo.json5");
    Res INLINE = Res.of("[1,2,3]");
    Res AUTO_DETECT = Res.of("{key: 'value'}");

    @Test
    public void assertTest() {
        assertStruct(INLINE, new int[]{1, 2, 3});
    }

    @Test
    public void asStringTest() {
        String templateAsString = AUTO_DETECT.asString();
        assertEquals(templateAsString, "{key: 'value'}");
    }

    @Test
    public void asDataTest() {
        Map data = (Map) AUTO_DETECT.asTemplate().toData();
        assertEquals(data.get("key"), "value");
    }
}
