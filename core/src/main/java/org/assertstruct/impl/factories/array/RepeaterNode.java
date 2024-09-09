package org.assertstruct.impl.factories.array;

import lombok.Getter;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.node.ScalarNode;

@Getter
public class RepeaterNode extends ScalarNode {
    TemplateNode repeatedNode;

    public RepeaterNode(TemplateNode repeatedNode, TemplateKey key, ExtToken token) {
        super(key, token);
        this.repeatedNode = repeatedNode;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        return false;
    }

    @Override
    public boolean isRepeaterFor(TemplateNode node) {
        return repeatedNode == node;
    }
}
