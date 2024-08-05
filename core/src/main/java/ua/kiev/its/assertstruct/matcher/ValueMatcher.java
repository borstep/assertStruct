package ua.kiev.its.assertstruct.matcher;

public interface ValueMatcher {

    boolean match(Object value, Matcher context);
}
