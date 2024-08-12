package ua.kiev.its.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.AbstractEvaluatorKey;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpelKey extends AbstractEvaluatorKey {
    Expression expression;
    SpelFactory spelFactory;

    public SpelKey(Expression expression, @NonNull String value, ExtToken token, SpelFactory spelFactory) {
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
