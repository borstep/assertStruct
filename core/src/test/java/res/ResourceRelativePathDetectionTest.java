package res;

import org.assertstruct.Res;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceRelativePathDetectionTest {

    @Test
    void fromRelativeToPackage() {
        Res res = Res.from("$$/res.txt");
        Assertions.assertEquals("package", res.asString());
    }
    @Test
    void fromRelativeToClass() {
        Res res = Res.from("$/res.txt");
        Assertions.assertEquals("class", res.asString());
    }
    @Test
    void fromRelativeToMethod() {
        Res res = Res.from("$/$.res.txt");
        Assertions.assertEquals("method", res.asString());
    }

    @Test
    void fromAbsolute() {
        Res res = Res.from("res.txt");
        Assertions.assertEquals("absolute", res.asString());
    }

    @Test
    void fromAbsoluteInFolder() {
        Res res = Res.from("res/res.txt");
        Assertions.assertEquals("package", res.asString());
    }

    @Test
    void of() {
        Assertions.assertEquals("text", Res.of("text").asString());
    }
}