package ua.kiev.its.assertstruct.impl.factories.any;

import lombok.NonNull;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.AbstractMatcherKey;

public class AnyKey extends AbstractMatcherKey {

    public AnyKey(@NonNull String value, ExtToken token) {
        super(value, token);
    }

    @Override
    public boolean match(String key, Matcher context) {
        return true;
    }
}
