package ua.kiev.its.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ua.kiev.its.assertstruct.service.KeyParser;
import ua.kiev.its.assertstruct.service.NodeParser;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
public class SpelParser implements KeyParser, NodeParser {

    public static final String PREFIX = "#";

    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext sharedContext = new StandardEvaluationContext();

    @Override
    public TemplateKey parseKey(String value, ExtToken token) {
        Expression expression = parser.parseExpression(value.substring(PREFIX.length()));
        return new SpelKey(expression, value, token, this);
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        Expression expression = parser.parseExpression(value.substring(PREFIX.length()));
        return new SpelNode(expression, templateKey, token, this);
    }
}
