import org.assertstruct.AssertStruct;
import org.assertstruct.AssertionStructFailedError;
import org.junit.jupiter.api.Test;

import static org.assertstruct.utils.MapUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomEnvTest {
    @Test
    public void assertCustom() {
        // Default behavior
        assertThrows(AssertionStructFailedError.class, () ->
                AssertStruct
                        .assertStruct("{a: 1, b: 2}", mapOf("a", 1, "b", 2, "c", 3))
        );

        // Custom env
        AssertStruct
                .with()
                .defaultIgnoreUnknown(true)
                .build()
                .assertStruct("{a: 1, b: 2}", mapOf("a", 1, "b", 2, "c", 3));
    }

}
