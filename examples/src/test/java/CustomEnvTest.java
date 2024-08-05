import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.AssertStruct;

public class CustomEnvTest {
    // TODO create example of define custom environment configuration
    AssertStruct assertStruct = new AssertStruct();

    @Test
    public void assertTest() {
        assertStruct.match("[1,5,3]", new int[]{1,2,3});
    }

    @Test
    public void test() {
    }
}
