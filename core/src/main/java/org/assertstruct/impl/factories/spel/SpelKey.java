package org.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.AbstractEvaluatorKey;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpelKey extends AbstractEvaluatorKey {
    Expression expression;
    SpelParser spelFactory;

    public SpelKey(Expression expression, @NonNull String value, ExtToken token, SpelParser spelFactory) {
        super(value, token);
        this.expression = expression;
        this.spelFactory = spelFactory;
    }

    @Override
    public Object evaluate(Object value, Matcher context) {
        StandardEvaluationContext sharedContext = spelFactory.getSharedContext();
        synchronized (sharedContext) {
            return expression.getValue(sharedContext,context.getCurrentSource());
        }
    }
}
