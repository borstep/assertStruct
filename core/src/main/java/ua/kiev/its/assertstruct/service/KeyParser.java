package ua.kiev.its.assertstruct.service;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;

public interface KeyParser extends Parser {
    TemplateKey parseKey(String value, ExtToken token);

}
