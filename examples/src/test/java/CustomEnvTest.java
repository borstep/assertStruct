import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.AssertStruct;
import ua.kiev.its.assertstruct.service.AssertStructService;

public class CustomEnvTest {
    @Test
    public void assertTest() {
        // TODO create example of define custom environment configuration
        AssertStruct
                .with()
                .defaultOrderedLists(false)
                .build()
                .match("[1,3,2]", new int[]{1, 2, 3});
    }

    @Test
    public void test() {
    }
}
