package ua.kiev.its.assertstruct.impl.factories.regexp;


import ua.kiev.its.assertstruct.config.KeyFactory;
import ua.kiev.its.assertstruct.config.NodeFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

import java.util.regex.Pattern;

public class RegexpFactory implements KeyFactory, NodeFactory {
    public static final RegexpFactory INSTANCE = new RegexpFactory();
    public static final String PREFIX = "$/";

    @Override
    public TemplateKey parseKey(String value, ExtToken token) {
        if (value.startsWith(PREFIX) && value.endsWith("/")) {
            String regexp = value.substring(PREFIX.length(), value.length() - 1);
            return new RegexpKey(Pattern.compile(regexp), value, token);
        }
        return null;
    }

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        if (value.startsWith(PREFIX) && value.endsWith("/")) {
            String regexp = value.substring(PREFIX.length(), value.length() - 1);
            return new RegexpNode(Pattern.compile(regexp), templateKey, token);
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
