package ua.kiev.its.assertstruct.impl.factories.variable;

import lombok.Value;
import ua.kiev.its.assertstruct.service.AssertStructService;
import ua.kiev.its.assertstruct.service.NodeParser;
import ua.kiev.its.assertstruct.service.Parser;
import ua.kiev.its.assertstruct.service.ParserFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

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
