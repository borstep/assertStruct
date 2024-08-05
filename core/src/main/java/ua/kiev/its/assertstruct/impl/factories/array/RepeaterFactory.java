package ua.kiev.its.assertstruct.impl.factories.array;

import ua.kiev.its.assertstruct.config.NodeFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

public class RepeaterFactory implements NodeFactory {
    public static final RepeaterFactory INSTANCE = new RepeaterFactory();
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
}
