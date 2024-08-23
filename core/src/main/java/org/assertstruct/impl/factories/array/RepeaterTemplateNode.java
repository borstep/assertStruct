package org.assertstruct.impl.factories.array;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.node.ScalarNode;

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
