package ua.kiev.its.assertstruct.impl.factories.regexp;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.node.ScalarNode;

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
