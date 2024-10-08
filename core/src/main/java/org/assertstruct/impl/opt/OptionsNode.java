package org.assertstruct.impl.opt;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.node.ScalarNode;

@Value
@EqualsAndHashCode(callSuper = true)
public class OptionsNode extends ScalarNode {
    String name;
    Object value;
    boolean isSubTree;

    public OptionsNode(String name, Object value, boolean isSubTree, ExtToken token) {
        super(null, token);
        this.name = name;
        this.value = value;
        this.isSubTree = isSubTree;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        return false;
    }

    @Override
    public boolean isConfig() {
        return true;
    }
}
