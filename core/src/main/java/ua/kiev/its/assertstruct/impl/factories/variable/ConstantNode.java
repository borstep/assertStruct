package ua.kiev.its.assertstruct.impl.factories.variable;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.matcher.ValueMatcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.node.ScalarNode;

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
