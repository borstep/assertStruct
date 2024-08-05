package ua.kiev.its.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringNode extends ValueNode {
    @Getter
    char quoteChar = '\"';
    @Getter
    boolean multiline = false; // TODO

    public StringNode(TemplateKey key, String value, ExtToken token) {
        super(key, value, token);
        quoteChar = token.getQuoteChar();
    }
}
