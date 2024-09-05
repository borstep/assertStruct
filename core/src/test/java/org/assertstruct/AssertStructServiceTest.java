package org.assertstruct;

import org.assertstruct.service.AssertStructService;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertstruct.utils.MapUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class AssertStructServiceTest {
    AssertStructService assertStructService = AssertStruct.getDefault();

    @Test
    void match() {
        AssertionFailedError ex = assertThrows(AssertionFailedError.class, () ->
                assertStructService.match("{key:1}"
                        , mapOf("key", 2), "Error message")
        );
    }
}