package org.assertstruct.impl.factories.regexp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.AbstractMatcherKey;

import java.util.regex.Pattern;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegexpKey extends AbstractMatcherKey {
    Pattern pattern;

    public RegexpKey(Pattern pattern, @NonNull String value, ExtToken token) {
        super(value, token);
        this.pattern = pattern;
    }

    @Override
    public boolean match(String key, Matcher context) {
        return pattern.matcher(key).matches();
    }
}
