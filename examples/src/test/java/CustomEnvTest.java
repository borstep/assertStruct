import org.junit.jupiter.api.Test;
import org.assertstruct.AssertStruct;

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
