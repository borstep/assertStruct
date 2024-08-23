package org.assertstruct.matcher;

public interface ValueMatcher {

    boolean match(Object value, Matcher context);
}
