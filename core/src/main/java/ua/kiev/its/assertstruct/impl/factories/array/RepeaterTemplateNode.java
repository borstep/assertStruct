package ua.kiev.its.assertstruct.impl.factories.array;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;
import ua.kiev.its.assertstruct.template.node.ScalarNode;

public class RepeaterTemplateNode extends ScalarNode {

    public RepeaterTemplateNode(TemplateKey key, ExtToken token) {
        super(key, token);
    }

    @Override
    public boolean match(Object value, Matcher context) {
        return false;
    }

    public static boolean isRepeater(TemplateNode node) {
        return node instanceof RepeaterTemplateNode;
    }

    public static boolean isNotRepeater(TemplateNode node) {
        return !(node instanceof RepeaterTemplateNode);
    }
}
