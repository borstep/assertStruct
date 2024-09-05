package org.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.node.ScalarNode;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Objects;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpelNode extends ScalarNode {
    Expression expression;
    SpelParser spelFactory;

    public SpelNode(Expression expression, TemplateKey key, ExtToken token, SpelParser spelFactory) {
        super(key, token);
        this.expression = expression;
        this.spelFactory = spelFactory;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        StandardEvaluationContext sharedContext = spelFactory.getSharedContext();
        synchronized (sharedContext) {
            Object currentSource = context.getCurrentSource();
            Object parentSource = context.getParentSource();
            Object expectedValue = expression.getValue(sharedContext, parentSource);
            return Objects.equals(expectedValue, currentSource) || Objects.equals(expectedValue, value);
        }
    }

}
