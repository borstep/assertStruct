package ua.kiev.its.assertstruct.service;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

public interface NodeParser extends Parser {
    TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token);

}
