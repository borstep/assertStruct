package org.assertstruct.template;

import org.assertstruct.AssertStruct;
import org.assertstruct.Res;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;

public class ListUnorderedTests extends ListSharedTests {
    public ListUnorderedTests() {
        super(AssertStruct.getDefault()
                .with()
                .defaultOrderedLists(false)
                .build()
        );
    }

    @Test
    void wrongElementsOrderOK() {
        checkOK(Res.of("[1, 2, 3]"), listOf(1, 3, 2));
    }

    @Test
    void wrongElementsOrder2OK() {
        checkOK(Res.of("[1, 2, 3, 4, 5, 6]"), listOf(6, 5, 4, 1, 3, 2));
    }

    @Test
    void wrongElementsOrderWithMatchOK() {
        checkOK(Res.of("[1, 'str', '$*']"), listOf(1, 3, "str"));
    }

    /**
     * False positive fail, when mask is to earlier in the list.
     * This is false positive fail, because $* can match any value, and it was wrongly matched to 3.
     * This leave last element of template with no match.
     * This is done for performance reasons and maybe changed in the future.
     * It's false positive, so will not cause passing broken test, and usually can be easily fixed by rearranging template.
     * It is recommended to put generic matchers at the end of the list.
     */
    @Test
    void wrongElementsOrderWithMatchFail() {
        checkFail(Res.of("[1, '$*', 3]"), Res.of("[1, '$*', 'str']"), listOf(1, 3, "str"));
    }

}
