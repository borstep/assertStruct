package org.assertstruct.template;

import org.assertstruct.Res;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;

public class TemplateListTest {
    @Test
    void emptyOK() {
        checkOK(Res.of("[]"), listOf());
    }
    @Test
    void arrayOK() {
        checkOK(Res.of("[1, 2, 3]"), listOf(1, 2, 3));
    }
    @Test
    void wrongElementsFail() {
        checkFail(Res.of("[1, 2, 3]"), Res.of("[1, 5, 3]"), listOf(1, 5, 3));
    }

    @Test
    void lessElementsFail() {
        checkFail(Res.of("[1, 2, 3]"), Res.of("[1, 2,]"), listOf(1, 2));
    }
    @Test
    void moreElementFail() {
        checkFail(Res.of("[1, 2, 3]"), Res.of("[1, 2, 3, 4]"), listOf(1, 2, 3, 4));
    }

    @Test
    void arrayOfArrayOk() {
        checkOK(Res.of("[[], []]"), listOf(listOf(), listOf()));
    }

    @Test
    void arrayOfArrayFail1() {
        checkFail(Res.of("[0, []]"), Res.of("[[], []]"), listOf(listOf(), listOf()));
    }

    @Test
    void arrayOfArrayFail2() {
        checkFail(Res.of("[[1], []]"), Res.of("[[], []]"), listOf(listOf(), listOf()));
    }

    @Test
    void anyOk() {
        checkOK(Res.of("[1, '$*', 3]"), listOf(1, 2, 3));
    }

    @Test
    void wrongElementWithAnyFail() { 
        checkFail(Res.of("[1, '$*', 3]"), Res.of("[1, '$*', 4]"), listOf(1, 2, 4));
    }

    @Test
    void oneRepeaterLongOk() { 
        checkOK(Res.of("[1, '$*', '$...']"), listOf(1, 2, 3, 4, 5));
    }
    @Test
    void oneRepeaterShortOk() { 
        checkOK(Res.of("[1, '$*', '$...']"), listOf(1, 2));
//        checkFail(Res.of("[1, '$*', '$...']"), Res.of("[1, 2, 3, 4]"), listOf(1, 2, 3, 4));
    }
    @Test
    void oneRepeaterShortFail() { 
        checkFail(Res.of("[1, '$*', '$...']"), Res.of("[1,]"), listOf(1));
    }
/*********************************************************************************************/

    @Test
    void arrayWithElWhichRepeatsOnlyOnceOK() {
        checkOK(Res.of("[1, 3, 6, 7, 7, '$...', 9, 8]"),
                listOf(1, 3, 6, 7, 7, 9, 8));
    }

    @Test
    void arrayWithElWhichRepeatsOnlyOnceAndThenFollowsByTheSameElOK() {
        checkOK(Res.of("[1, 3, 6, 7, 7, '$...', 7, 9, 8]"),
                listOf(1, 3, 6, 7, 7, 7, 9, 8));
    }

    @Test
    void arrayAnyElementsRepeatThenGivenElementRepeatsOK() {
        checkOK(Res.of("[1, 3, 6, '$*', '$...', 9, 8, 7, '$...', 7, 9, 8]"),
                listOf(1, 3, 6, 0, 0, 0, 0, 9, 8, 7, 7, 7, 9, 8));
    }

    @Test
    void doubleRepeaterOK() {
        checkOK(Res.of("[1, 3, 6, '$*', '$...', 9, 8, 7, '$...', 9, 8]"),
                listOf(1, 3, 6, 0, 0, 0, 0, 9, 8, 7, 7, 7, 9, 8));
    }

    @Test
    void arrayOK1() {
        checkOK(Res.of("[1, 3, 6, '$*', '$...', 9, '$*', 7, '$...', 7, '$*', 8]"),
                listOf(1, 3, 6, 0, 0, 0, 0, 9, 9, 8, 7, 7, 7, 9, 8));
    }

    @Test
    void arrayOK2() {
        checkOK(Res.of("[1, 3, 6, '$*', '$...', 9, 8, 7, '$...', 7, 9, 8]"),
                listOf(1, 3, 6, 0, 0, 0, 0, 9, 9, 8, 7, 7, 7, 9, 8));
    }

    @Test
    void arrayOK3() {
        checkOK(Res.of("[1, 3, 6, 7, '$*', '$...', 9, 8]"),
                listOf(1, 3, 6, 7, 11, 13, 0, 9, 8));
    }

    @Test
    void arrayOK4() {
        checkOK(Res.of("[1, 3, 6, 7, '$*', '$...', 9, 8]"),
                listOf(1, 3, 6, 7, 11, 13, 0, 9, 9, 8));
    }

    @Test
    void arrayOK5() {
        checkOK(Res.of("[1, 3, 6, 7, '$*', '$...', '$*']"),
                listOf(1, 3, 6, 7, 7, 7, 9));
    }

    @Test
    void anyElementsArrayOK() {
        checkOK(Res.of("['$*', '$...']"),
                listOf(1, 3, 6, 7, 7, 7, 9));
    }

    @Test
    void arrayLookAheadEndsWithAnyOK() {
        checkOK(Res.of("['$*', '$...', 7, 9, '$*']"),
                listOf(1, 3, 6, 7, 7, 9, 7));
    }

    @Test
    void arrayLookAheadStartsEndsWithAnyOK() {
        checkOK(Res.of("['$*', '$...', '$*', 7, 7, 9]"),
                listOf(1, 3, 6, 7, 7, 7, 9));
    }

    @Test
    void arrayTemplateLongerThanActualArrayDueToRepeatWildcardOK() {
        checkOK(Res.of("[5, '$*', '$...']"),
                listOf(5, 7));
    }

    @Test
    void repeaterFail() {
        checkFail(Res.of("[1, 3, '$...', 7]"),
                Res.of("[1, 3, '$...', 6, 7, 0]"),
                listOf(1, 3, 6, 7, 0));
    }
}
