package ua.kiev.its.assertstruct.impl.factories.array;

import ua.kiev.its.assertstruct.service.AssertStructService;
import ua.kiev.its.assertstruct.service.NodeParser;
import ua.kiev.its.assertstruct.service.Parser;
import ua.kiev.its.assertstruct.service.ParserFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

public class RepeaterParser implements NodeParser, ParserFactory {
    public static final RepeaterParser INSTANCE = new RepeaterParser();
    public static final String PREFIX = "$...";

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        if (value.equals(PREFIX)) {
            return new RepeaterTemplateNode(templateKey, token);
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
