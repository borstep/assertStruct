package org.assertstruct.impl.factories.variable;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.matcher.ValueMatcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.node.ScalarNode;

import java.util.Objects;

@Value
@EqualsAndHashCode(callSuper = false)
public class ConstantNode extends ScalarNode {
    /**
     * Constant name
     */
    String name;

    public ConstantNode(String name, TemplateKey key, ExtToken token) {
        super(key, token);
        this.name = name;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        Object constant = context.getEnv().getConfig().getConstantService().getConstant(name);
        if (constant instanceof ValueMatcher) {
            return ((ValueMatcher) constant).match(value, context);
        } else {
            return Objects.equals(constant, value);
        }
    }
}
