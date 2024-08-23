package org.assertstruct.template;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.ValueMatcher;

public abstract class MatcherNode implements ValueMatcher {
    ExtToken token;

    public MatcherNode(ExtToken token) {
        this.token = token;
    }

}
