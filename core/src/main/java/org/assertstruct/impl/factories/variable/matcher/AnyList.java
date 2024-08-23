package org.assertstruct.impl.factories.variable.matcher;

import org.assertstruct.matcher.Matcher;
import org.assertstruct.matcher.ValueMatcher;

import java.util.List;

public class AnyList implements ValueMatcher {
    public static final String NAME = "[*]";

    @Override
    public boolean match(Object value, Matcher context) {
        return value instanceof List;
    }
}
