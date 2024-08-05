package ua.kiev.its.assertstruct.config;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;

public interface KeyFactory extends ParsingFactory {
    TemplateKey parseKey(String value, ExtToken token);

}
