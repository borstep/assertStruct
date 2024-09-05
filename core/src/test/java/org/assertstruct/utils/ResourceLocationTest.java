package org.assertstruct.utils;

import org.assertstruct.Res;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceLocationTest {
    public static final Res CONTENT = Res.of(" text");
    public static final Res FILE = Res.from("res/res.txt");

    @Test
    void contentResourceLocationAt() {
        ResourceLocation location = CONTENT.getLocation();
        assertEquals("at org.assertstruct.utils.ResourceLocationTest(ResourceLocationTest.java:9)", location.at());
    }

    @Test
    void fileResourceLocationAt() {
        ResourceLocation location = FILE.getLocation();
        assertEquals("at /C:/Boris/PRJ/pet/assertstruct-core/core/target/test-classes/res/res.txt (res.txt:0)", location.at());
    }

    @Test
    void contentResourceSrc() {
        String uri = CONTENT.getSourceLocation().fileURI();
        uri = uri.replace('\\', '/');
        assertTrue(uri.contains("/core/src/test/java/org/assertstruct/utils/ResourceLocationTest.java:"));
    }
}