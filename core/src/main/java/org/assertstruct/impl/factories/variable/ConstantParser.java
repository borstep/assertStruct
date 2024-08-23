package org.assertstruct.impl.factories.variable;

import lombok.Value;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.NodeParser;
import org.assertstruct.service.Parser;
import org.assertstruct.service.ParserFactory;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;

@Value
public class ConstantParser implements NodeParser, ParserFactory {
    public static final ConstantParser INSTANCE = new ConstantParser();
    public static final String PREFIX = "$";

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        return new ConstantNode(value.substring(PREFIX.length()), templateKey, token);
    }

    @Override
    public int priority() {
        return Integer.MAX_VALUE - 1;
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
