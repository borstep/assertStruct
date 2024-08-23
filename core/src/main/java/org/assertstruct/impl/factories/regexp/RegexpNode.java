package org.assertstruct.impl.factories.regexp;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.node.ScalarNode;

import java.util.regex.Pattern;

public class RegexpNode extends ScalarNode {
    Pattern pattern;

    public RegexpNode(Pattern pattern, TemplateKey key, ExtToken token) {
        super(key, token);
        this.pattern = pattern;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        if (value == null) {
            return false;
        }
        String str;
        if (value instanceof String) {
            str = (String) value;
        } else {
            str = value.toString();
        }
        return pattern.matcher(str).matches();
    }

}
