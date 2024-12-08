package org.assertstruct.utils;

import org.assertstruct.Res;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ResourceLocationTest {
    public static final Res CONTENT = Res.of(" text");
    public static final Res FILE = Res.from("res/res.txt");

    @Test
    void contentResourceLocationAt() {
        ResourceLocation location = CONTENT.getLocation();
        assertEquals("at org.assertstruct.utils.ResourceLocationTest(ResourceLocationTest.java:10)", location.at());
    }

    @Test
    void fileResourceLocationAt() {
        ResourceLocation location = FILE.getLocation();
        assertThat(location.at()).matches("at .+/core/target/test-classes/res/res\\.txt \\(res\\.txt:0\\)");
    }

    @Test
    void contentResourceSrc() {
        String uri = CONTENT.getSourceLocation().fileURI();
        uri = uri.replace('\\', '/');
        assertTrue(uri.contains("/core/src/test/java/org/assertstruct/utils/ResourceLocationTest.java:"));
    }
}