package ua.kiev.its.assertstruct.impl.factories.any;

import ua.kiev.its.assertstruct.config.KeyFactory;
import ua.kiev.its.assertstruct.config.NodeFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

public class ShortAnyFactory implements KeyFactory, NodeFactory {

    public static final ShortAnyFactory INSTANCE = new ShortAnyFactory();
    public static final String PREFIX = "$*";

    @Override
    public TemplateKey parseKey(String value, ExtToken token) {
        if (value.equals(PREFIX)) {
            return new AnyKey(value, token);
        }
        return null;
    }

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        if (value.equals(PREFIX)) {
            return new AnyNode(templateKey, token);
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
