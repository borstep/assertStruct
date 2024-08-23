package org.assertstruct.service;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.TemplateKey;

public interface KeyParser extends Parser {
    TemplateKey parseKey(String value, ExtToken token);

}
