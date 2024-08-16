package ua.kiev.its.assertstruct.impl.factories.variable.matcher;

import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.matcher.ValueMatcher;

import java.util.List;

public class AnyList implements ValueMatcher {
    public static final String NAME = "[*]";

    @Override
    public boolean match(Object value, Matcher context) {
        return value instanceof List;
    }
}