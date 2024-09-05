import org.assertstruct.AssertStruct;
import org.junit.jupiter.api.Test;

public class CustomEnvTest {
    @Test
    public void assertCustom() {
        // TODO create example of define custom environment configuration
        AssertStruct
                .with()
                .defaultOrderedLists(false)
                .build()
                .match("[1,3,2]", new int[]{1, 2, 3});
    }

}
