package ua.kiev.its.assertstruct.impl.factories.any;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.node.ScalarNode;


public class AnyNode extends ScalarNode {

    public AnyNode(TemplateKey key, ExtToken token) {
        super(key, token);
    }

    @Override
    public boolean match(Object value, Matcher context) {
        return true;
    }
}
