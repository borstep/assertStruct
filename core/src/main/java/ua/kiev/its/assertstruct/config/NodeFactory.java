package ua.kiev.its.assertstruct.config;

import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

public interface NodeFactory extends ParsingFactory {
    TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token);

}
