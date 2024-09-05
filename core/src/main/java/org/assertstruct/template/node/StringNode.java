package org.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.TemplateKey;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StringNode extends ValueNode {
    char quoteChar;
    boolean multiline = false; // TODO

    public StringNode(TemplateKey key, String value, ExtToken token) {
        super(key, value, token);
        quoteChar = token.getQuoteChar();
    }
}
