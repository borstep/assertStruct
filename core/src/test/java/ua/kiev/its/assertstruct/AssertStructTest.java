package ua.kiev.its.assertstruct;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;
import static ua.kiev.its.assertstruct.TestUtils.mapOf;

class AssertStructTest {
    AssertStruct assertStruct = AssertStructUtils.getDefault();

    @Test
    void match() {
        AssertionFailedError ex = assertThrows(AssertionFailedError.class, () ->
                assertStruct.match("{key:1}"
                        , mapOf("key", 2), "Error message")
        );
    }
}