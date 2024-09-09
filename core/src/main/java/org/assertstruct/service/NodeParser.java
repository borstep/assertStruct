package org.assertstruct.service;

import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.TemplateParser;

public interface NodeParser extends Parser {
    TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token, TemplateParser templateParser);

}
