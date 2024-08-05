package ua.kiev.its.assertstruct.impl.factories.variable;

import lombok.Value;
import ua.kiev.its.assertstruct.AssertStruct;
import ua.kiev.its.assertstruct.config.NodeFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

@Value
public class ConstantFactory implements NodeFactory {
    public static final String PREFIX = "$";
    AssertStruct env;

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
}
