package org.assertstruct.impl.factories.regexp;


import org.assertstruct.service.*;
import org.assertstruct.service.*;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;

import java.util.regex.Pattern;

public class RegexpParser implements KeyParser, NodeParser, ParserFactory {

    public static final RegexpParser INSTANCE = new RegexpParser();
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

    @Override
    public Parser buildParser(AssertStructService assertStructService) {
        return INSTANCE;
    }
}
