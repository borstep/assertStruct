package org.assertstruct.impl.factories.any;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.service.*;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.TemplateParser;

public class ShortAnyParser implements KeyParser, NodeParser, ParserFactory {

    public static final ShortAnyParser INSTANCE = new ShortAnyParser();
    public static final String PREFIX = "$*";

    @Override
    public TemplateKey parseKey(String value, ExtToken token) {
        if (value.equals(PREFIX)) {
            return new AnyKey(value, token);
        }
        return null;
    }

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token, TemplateParser templateParser) {
        if (value.equals(PREFIX)) {
            return new AnyNode(templateKey, token);
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
