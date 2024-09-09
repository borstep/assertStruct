package org.assertstruct.template;

import org.assertstruct.AssertStruct;
import org.assertstruct.Res;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;

public class ListOrderedTests extends ListSharedTests {
    public ListOrderedTests() {
        super(AssertStruct.getDefault());
    }

    @Test
    void wrongElementsOrderFail() {
        checkFail(Res.of("[1, 2, 3]"), Res.of("[1, 3, 2]"), listOf(1, 3, 2));
    }

}
