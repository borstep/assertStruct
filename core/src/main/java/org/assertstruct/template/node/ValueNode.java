package org.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.matcher.ValueMatcher;
import org.assertstruct.template.DataNode;
import org.assertstruct.template.TemplateKey;

import java.util.Objects;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ValueNode extends ScalarNode implements ValueMatcher, DataNode<Object> {
    Object value;

    public ValueNode(TemplateKey key, Object value, ExtToken token) {
        super(key, token);
        this.value = value;
    }

    @Override
    public boolean match(Object actual, Matcher context) {
        return Objects.equals(value, actual);
    }

    @Override
    public boolean isDataNode() {
        return true;
    }

    @Override
    public Object toData() {
        return getValue();
    }
}