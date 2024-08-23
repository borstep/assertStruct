package org.assertstruct.impl.factories.any;

import lombok.NonNull;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.AbstractMatcherKey;

public class AnyKey extends AbstractMatcherKey {

    public AnyKey(@NonNull String value, ExtToken token) {
        super(value, token);
    }

    @Override
    public boolean match(String key, Matcher context) {
        return true;
    }
}
