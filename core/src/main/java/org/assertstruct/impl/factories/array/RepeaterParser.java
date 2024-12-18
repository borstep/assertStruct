package org.assertstruct.impl.factories.array;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.NodeParser;
import org.assertstruct.service.Parser;
import org.assertstruct.service.ParserFactory;
import org.assertstruct.template.StructTemplateNode;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.TemplateParser;

public class RepeaterParser implements NodeParser, ParserFactory {
    public static final RepeaterParser INSTANCE = new RepeaterParser();
    public static final String PREFIX = "$...";

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token, TemplateParser templateParser) {
        if (value.equals(PREFIX)) {
            StructTemplateNode parent = templateParser.currentNode();
            if (parent.isArray()) {
                return new RepeaterNode(parent.asArray().getLast(), templateKey, token);
            }
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
