package ua.kiev.its.assertstruct.impl.factories.value;

import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.matcher.ValueMatcher;

public class AnyValueMatcher implements ValueMatcher {

    @Override
    public boolean match(Object value, Matcher context) {
        return false;
    }
}
