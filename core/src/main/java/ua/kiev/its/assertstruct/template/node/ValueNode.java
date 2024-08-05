package ua.kiev.its.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.matcher.ValueMatcher;
import ua.kiev.its.assertstruct.template.DataNode;
import ua.kiev.its.assertstruct.template.TemplateKey;

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