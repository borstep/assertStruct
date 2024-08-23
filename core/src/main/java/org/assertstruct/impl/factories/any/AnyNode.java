package org.assertstruct.impl.factories.any;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.node.ScalarNode;


public class AnyNode extends ScalarNode {

    public AnyNode(TemplateKey key, ExtToken token) {
        super(key, token);
    }

    @Override
    public boolean match(Object value, Matcher context) {
        return true;
    }
}
