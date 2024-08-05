package ua.kiev.its.assertstruct.template;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.ValueMatcher;

public abstract class MatcherNode implements ValueMatcher {
    ExtToken token;

    public MatcherNode(ExtToken token) {
        this.token = token;
    }

}
