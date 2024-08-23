package org.assertstruct;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.assertstruct.service.AssertStructService;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertstruct.utils.MapUtils.mapOf;

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