package org.assertstruct.utils;

import org.junit.jupiter.api.Test;
import org.assertstruct.Res;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceLocationTest {
    public static final Res CONTENT = Res.of(" text");
    public static final Res FILE = Res.from("res/res.txt");

    @Test
    void contentResourceLocationAt() {
        ResourceLocation location = CONTENT.getLocation();
        assertEquals("at org.assertstruct.utils.ResourceLocationTest(ResourceLocationTest.java:10)", location.at());
//        System.out.println(location);
//        System.out.println(location.getResource());
//        System.out.println(location.fileURI());
//        System.out.println(location.at());
    }

    @Test
    void fileResourceLocationAt() {
        ResourceLocation location = FILE.getLocation();
        assertEquals("at /C:/Boris/PRJ/pet/assertstruct-core/core/target/test-classes/res/res.txt (res.txt:0)", location.at());
//        System.out.println(res.getLocation());
//        System.out.println(res.getLocation().fileURI());
//        System.out.println(res.getLocation().at());
//
//        System.out.println("----------------");
//        System.out.println(res.getLocation().getResource());
//        System.out.println("/C:/Boris/PRJ/pet/assertstruct-core/core/target/test-classes/res/res.txt ");
//        System.out.println("at /C:/Boris/PRJ/pet/assertstruct-core/core/target/test-classes/res/res.txt (res.txt:0)");
    }

    @Test
    void contentResourceSrc() {
        String uri = CONTENT.getSourceLocation().fileURI();
        uri = uri.replace('\\', '/');
        assertTrue(uri.contains("/core/src/test/java/org/assertstruct/utils/ResourceLocationTest.java:"));
    }
}