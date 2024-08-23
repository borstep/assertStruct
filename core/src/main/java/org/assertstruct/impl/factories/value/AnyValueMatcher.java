package org.assertstruct.impl.factories.value;

import org.assertstruct.matcher.Matcher;
import org.assertstruct.matcher.ValueMatcher;

public class AnyValueMatcher implements ValueMatcher {

    @Override
    public boolean match(Object value, Matcher context) {
        return false;
    }
}
